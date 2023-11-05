package info.kfgodel.contable.printer

import info.kfgodel.contable.accountant.AccountantRecord
import info.kfgodel.contable.accountant.AccountantReport
import info.kfgodel.contable.accountant.PortfolioOperation
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.concepts.OperationType
import info.kfgodel.contable.toISODate
import info.kfgodel.contable.valued.AssetBalance
import info.kfgodel.contable.valued.ValueChange

/**
 * This class outputs the accountant report content as a CSV or TSV file into the stdout
 * Date: 29/8/23 - 23:59
 */
class CsvPrinter(private val report: AccountantReport) {
  fun print() {
    // This needs to be pre-calculated. FIX-ME
    val startingValuation = report.valuationAtStart()
    val endingValuation = report.valuationAtEnd()


    println("* Año: ${report.year}")
    println(
      asCsvRow(
        listOf(
          "Wallet",
          "Operado en",
          "Tipo",
          "Asset",
          "Cantidad",
          "Moneda",
          "Importe",
          "Cuenta externa",
          "Parte",
          "Precio original",
          "Precio operado",
          "Dif Precio",
          "Fecha original",
          "Valuacion"
        )
      )
    )

    val records = report.records()
    for (record in records) {
      println(asCsvOperation(record))
      if (record.operation.type == OperationType.WITHDRAW) {
        println(asWitdraw(record))
      }
      val operationChanges = record.changes
      for (change in operationChanges) {
        println(asCsvPart(change))
      }
    }

    println("Saldos iniciales:")
    printBalances(startingValuation.balances())
    println("Saldos finales:")
    printBalances(endingValuation.balances())
  }

  private fun printBalances(balances: List<AssetBalance>) {
    balances.forEach { balance ->
      println(asCsvRow(listOf(
        "-",
        balance.asset().unit,
        balance.asset().amount.toString(),
        balance.value().unit,
        balance.value().amount.toString()
      )))
    }
  }

  private fun asWitdraw(record: AccountantRecord): String {
    return asCsvRow(
      listOf(
        // Wallet
        "",
        // Operado en
        "",
        // Tipo
        "\\",
        // Asset
        "",
        // Cantidad
        "",
        // Moneda
        "",
        // Importe
        "",
        // Cuenta externa
        "",
        // Parte
        record.operation.exchange.asset().amount.toString(),
        // Precio original
        "",
        // Nuevo Precio
        record.valuationDifference().amount.toString(),
        // Dif Precio
        "",
        // Fecha original
        "",
        // Valuacion
        ""
      )
    )

  }

  private fun asCsvPart(change: ValueChange<PortfolioOperation>): String {
    return asCsvRow(
      listOf(
        // Wallet
        "",
        // Operado en
        change.replacement().originalOperation.moment.toISODate(),
        // Tipo
        "\\",
        // Asset
        "",
        // Cantidad
        "",
        // Moneda
        "",
        // Importe
        "",
        // Cuenta externa
        "",
        // Parte
        change.replaced().asset().amount.toString(),
        // Precio original
        change.replaced().value().amount.toString(),
        // Nuevo Precio
        change.replacement().value().amount.toString(),
        // Dif Precio
        change.value().amount.toString(),
        // Fecha original
        change.replaced().originalOperation.moment.toISODate(),
        // Valuacion
        ""
      )
    )
  }

  private fun asCsvOperation(record: AccountantRecord): String {
    val operation = record.operation
    val tipoOperacion = when (operation.type) {
      OperationType.TRANSFER -> "TRANSFERENCIA"
      OperationType.WITHDRAW -> "RETIRO"
      OperationType.DEPOSIT -> "DEPOSITO"
      OperationType.COMISION -> "COMISION"
      OperationType.INTEREST -> "INTERESES"
      OperationType.SELL -> "VENTA"
      OperationType.BUY -> "COMPRA"
      else -> {
        throw Error("Tipo de operacion no conocida: " + operation.type)
      }
    }
    return asCsvRow(
      listOf(
        // Wallet
        asAccountName(operation.mainAccount),
        // Operado en
        operation.moment.toISODate(),
        // Tipo
        tipoOperacion,
        // Asset
        operation.exchange.asset().unit,
        // Cantidad
        operation.exchange.asset().amount.toString(),
        // Moneda
        operation.exchange.value().unit,
        // Importe
        operation.exchange.value().amount.toString(),
        // Cuenta externa
        asAccountName(operation.externalAccount),
        // Parte
        "",
        // Precio original
        "",
        // Nuevo Precio
        "",
        // Dif Precio
        "",
        // Fecha original
        "",
        // Valuacion
        record.endingValuation.amount.toString(),
      )
    )
  }

  private fun asAccountName(account: String): String {
    return if (account == Operation.UNDEFINED_ACCOUNT) "" else account
  }

  private fun asCsvRow(array: Iterable<String>): String {
    return array.joinToString("\t")
  }

  private fun asCsvBalances(balances: List<AssetBalance>): String {
    return asCsvRow(balances.flatMap { balance ->
      listOf(
        balance.toString()
      )
    })
  }

}