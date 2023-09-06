package info.kfgodel.contable.reader

import com.google.common.base.Splitter
import info.kfgodel.contable.ARS
import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.USD
import info.kfgodel.contable.concepts.Exchange
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.concepts.OperationType
import info.kfgodel.contable.of
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

/**
 * This class represents a reader of the lombard operation report that converts each line into an operation
 * Date: 2/7/22 - 22:27
 */
class PatagoniaReportReader : OperationsReader {
  private val LINE_REGEX =
    """(\d\d/\d\d/\d\d\d\d) (RESCATE|SUSCRIPCION) (\d[\d.,]*) (\d+) (\d[\d.,]*) (.{1,3})""".toRegex()
  private val DECIMAL_FORMATTER = (NumberFormat.getInstance(Locale.GERMAN) as DecimalFormat)
    .also { formatter -> formatter.setParseBigDecimal(true) }
  private val DATE_FORMATTER = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE)

  private val operations = mutableListOf<Operation>()

  override fun operations(): List<Operation> {
    return operations.reversed() // File contains the newest first, we revert to do chronological order
  }

  fun addReport(lombardReport: String) {
    Splitter.on("\n")
      .split(lombardReport)
      .forEach { reportLine -> addLine(reportLine) }
  }

  private fun addLine(lombardReport: String) {
    val matchResult = LINE_REGEX.matchAt(lombardReport, 0)
    if (matchResult == null || matchResult.groups.size != 7) {
      throw IllegalArgumentException("Report line has unexpected format. Expected[08/05/2019 SUSCRIPCION 1,04936100 952 998,99 u\$s] got:[${lombardReport}]")
    }
    val readOperation = parseOperation(matchResult)
    operations.add(readOperation)
  }

  private fun parseOperation(matchResult: MatchResult): Operation {
    val date = matchResult.groupValues[1]
    val type = matchResult.groupValues[2]
    val quantity = matchResult.groupValues[4]
    val amount = matchResult.groupValues[5]
    val currency = matchResult.groupValues[6]
    val operationType = parseOperationType(type)
    val operationExchange = parseExchange(quantity, amount, currency, operationType)
    val operationMoment = parseMoment(date)
    return Operation(operationType, operationExchange, operationMoment)
  }

  private fun parseMoment(date: String): LocalDateTime {
    val parsedDate = DATE_FORMATTER.parse(date)
    return Instant.ofEpochMilli(parsedDate.time)
      .atZone(ZoneId.systemDefault())
      .toLocalDateTime()
  }

  private fun parseExchange(quotes: String, money: String, currency: String, operationType: OperationType): Exchange {
    var quoteQuantity = DECIMAL_FORMATTER.parse(quotes) as BigDecimal
    var amountMoney = DECIMAL_FORMATTER.parse(money) as BigDecimal
    if (operationType.equals(OperationType.SELL)) {
      quoteQuantity = quoteQuantity.negate()
    }else{
      amountMoney = amountMoney.negate()
    }
    val currencyUnit = parseCurrency(currency)
    return quoteQuantity.of(LOMBARD).at(amountMoney.of(currencyUnit))
  }

  private fun parseCurrency(currency: String): String {
    return when (currency) {
      "u\$s" -> USD
      "$" -> ARS
      else -> throw java.lang.IllegalArgumentException("Unexpected currency[${currency}]")
    }
  }

  private fun parseOperationType(subscriptionType: String): OperationType {
    return when (subscriptionType) {
      "SUSCRIPCION" -> OperationType.BUY
      "RESCATE" -> OperationType.SELL
      else -> throw IllegalArgumentException("Unexpected subscription type[${subscriptionType}]")
    }
  }

  override fun addReportFile(reportFile: File): PatagoniaReportReader {
    val reportContent = Files.readString(reportFile.toPath())
    addReport(reportContent)
    return this
  }
}