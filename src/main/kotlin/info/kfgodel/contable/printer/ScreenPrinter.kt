package info.kfgodel.contable.printer

import info.kfgodel.contable.accountant.AccountantRecord
import info.kfgodel.contable.accountant.AccountantReport
import info.kfgodel.contable.operations.Operation
import info.kfgodel.contable.valued.ValueChange
import info.kfgodel.contable.valued.changeSymbolFor

/**
 * This class represents a printer for outputting an accountant report into the screen
 * Date: 27/8/23 - 19:01
 */
class ScreenPrinter(private val report: AccountantReport) {
  fun print() {
    println("* Año: ${report.year}");
    val startingValuation = report.valuationAtStart();
    val endingValuation = report.valuationAtEnd();
    println("Saldos iniciales:${startingValuation.balances()} y finales: ${endingValuation.balances()}")

    val records = report.records()
    for (record in records) {
      printAsOperation(record)
      val operationChanges = record.changes
      for (change in operationChanges) {
        printlnAsChange(change)
      }
    }
    val totalProfitOrLoss = endingValuation.totalProfitOrLoss()
    println("Resultado final: ${changeSymbolFor(totalProfitOrLoss.amount)} $totalProfitOrLoss")

  }

  private fun printlnAsChange(change: ValueChange) {
    val replaced = change.replaced()
    val replacement = change.replacement()
    println("\t\\ ${changeSymbolFor(change.value().amount)} ${change.value().amount}: ${replaced.asset().amount} @ ${replacement.value().amount} desde ${replaced.value().amount} en ${replaced.moment}")
  }

  private fun printAsOperation(record: AccountantRecord) {
    val operation = record.operation
    val asset = operation.exchange.asset()
    val value = operation.exchange.value()
    println(buildString {
      if (operation.mainAccount != Operation.UNDEFINED_ACCOUNT) {
        append(operation.mainAccount)
      }
      append("- ")
      append(operation.moment)
      append(" ")
      append(operation.type)
      append(" ")
      append(asset.unit)
      append(" ")
      append(asset.amount)
      append(" @ ")
      append(value.unit)
      append(" ")
      append(value.amount)
      if (operation.externalAccount != Operation.UNDEFINED_ACCOUNT) {
        append(" en ")
        append(operation.externalAccount)
      }
    })
  }
}
