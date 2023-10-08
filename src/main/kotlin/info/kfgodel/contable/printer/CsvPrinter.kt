package info.kfgodel.contable.printer

import info.kfgodel.contable.accountant.AccountantRecord
import info.kfgodel.contable.accountant.AccountantReport
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
    println("* Año: ${report.year}")
    val startingValuation = report.valuationAtStart()
    println("Saldos iniciales:\t" + asCsvBalances(startingValuation.balances()))
    val endingValuation = report.valuationAtEnd()
    println("Saldos finales:\t" + asCsvBalances(endingValuation.balances()))
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
      val operationChanges = record.changes
      for (change in operationChanges) {
        println(asCsvPart(change))
      }
    }
  }

  private fun asCsvPart(change: ValueChange): String {
    return asCsvRow(
      listOf(
        // Wallet
        "",
        // Operado en
        change.replacement().moment.toISODate(),
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
        change.replaced().moment.toISODate(),
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
        record.valuation.amount.toString(),
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
        balance.asset().unit,
        balance.asset().amount.toString()
      )
    })
  }

}