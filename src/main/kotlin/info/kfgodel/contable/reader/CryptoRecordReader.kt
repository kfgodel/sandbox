package info.kfgodel.contable.reader

import com.google.common.base.Splitter
import info.kfgodel.contable.ARS
import info.kfgodel.contable.BTC
import info.kfgodel.contable.DAI
import info.kfgodel.contable.ETH
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

/**
 * This class represents a reader of crypto transactions recorded over time
 * Date: 27/8/23 - 14:22
 */
class CryptoRecordReader: OperationsReader {
  private val LINE_REGEX = """(\w+)\t(\d{4}-\d{2}-\d{2})\t(COMPRA|VENTA|DEPOSITO|RETIRO|TRANSFER|COMISION|INTERESES)\t(\w+)?\t(-?\d[\d.,]*)?\t(\w+)\t(-?\d[\d.,]*)\t?(.+)?""".toRegex()
  private val DECIMAL_FORMATTER = (NumberFormat.getInstance(Locale.US) as DecimalFormat)
    .also { formatter -> formatter.setParseBigDecimal(true) }
  private val DATE_FORMATTER: DateFormat = SimpleDateFormat("yyyy-MM-dd")

  private val operations = mutableListOf<Operation>()

  override fun operations(): List<Operation> {
    return operations.reversed() // File contains the newest first, we revert to do chronological order
  }

  fun addReport(operationLine: String) {
    Splitter.on("\n")
      .split(operationLine)
      .forEach { reportLine -> addLine(reportLine) }
  }

  private fun addLine(operationLine: String) {
    if(operationLine.isBlank()){
      return
    }
    val matchResult = LINE_REGEX.matchAt(operationLine, 0)
    if (matchResult == null || matchResult.groups.size != 9) {
      throw IllegalArgumentException("Report line has unexpected format. Expected[Buenbit\t2020-05-13\tTRANSFER\tDAI\t941.06\tDAI\t941.06\tSatoshi] got:[${operationLine}]")
    }
    val readOperation = parseOperation(matchResult)
    operations.add(readOperation)
  }

  private fun parseOperation(matchResult: MatchResult): Operation {
    val mainAccount = matchResult.groupValues[1]
    val date = matchResult.groupValues[2]
    val type = matchResult.groupValues[3]
    val asset = matchResult.groupValues[4]
    val quantity = matchResult.groupValues[5]
    val currency = matchResult.groupValues[6]
    val amount = matchResult.groupValues[7]
    val externalAccount = matchResult.groupValues[8].ifBlank { Operation.UNDEFINED_ACCOUNT}
    val operationType = parseOperationType(type)
    val operationExchange = parseExchange(quantity, asset, amount, currency)
    val operationMoment = parseMoment(date)
    return Operation(operationType, operationExchange, operationMoment, mainAccount, externalAccount)
  }

  private fun parseMoment(date: String): LocalDateTime {
    val parsedDate = DATE_FORMATTER.parse(date)
    return Instant.ofEpochMilli(parsedDate.time)
      .atZone(ZoneId.systemDefault())
      .toLocalDateTime()
  }

  private fun parseExchange(quantity: String, assetName: String, amount: String, currency: String): Exchange {
    val amountMoney = DECIMAL_FORMATTER.parse(amount) as BigDecimal
    val currencyUnit = parseCurrency(currency)
    val price = amountMoney.of(currencyUnit)
    if(quantity.isBlank()){
      // It's an operation that has no asset. Use value a exchanged asset
      return price.exchanged()
    }
    val assetQuantity = DECIMAL_FORMATTER.parse(quantity) as BigDecimal
    val assetUnit = parseCurrency(assetName)
    val asset = assetQuantity.of(assetUnit)
    return asset.at(price)
  }

  private fun parseCurrency(currency: String): String {
    return when(currency){
      "USD" -> USD
      "ARS" -> ARS
      "BTC" -> BTC
      "DAI" -> DAI
      "ETH" -> ETH
      else -> throw java.lang.IllegalArgumentException("Unexpected currency[${currency}]")
    }
  }

  private fun parseOperationType(subscriptionType: String): OperationType {
    return when(subscriptionType){
      "COMPRA" -> OperationType.BUY
      "VENTA" -> OperationType.SELL
      "INTERESES" -> OperationType.INTEREST
      "COMISION" -> OperationType.COMISION
      "RETIRO" -> OperationType.WITHDRAW
      "DEPOSITO" -> OperationType.DEPOSIT
      "TRANSFER" -> OperationType.TRANSFER
      else -> throw IllegalArgumentException("Unexpected operation type[${subscriptionType}]")
    }
  }

  override fun addReportFile(reportFile: File): CryptoRecordReader {
    val reportContent = Files.readString(reportFile.toPath())
    addReport(reportContent)
    return this
  }
}
