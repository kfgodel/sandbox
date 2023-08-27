package info.kfgodel.contable.printer

import info.kfgodel.contable.accountant.AccountantReport
import info.kfgodel.contable.operations.OperationType
import info.kfgodel.contable.valued.changeSymbolFor

/**
 * This class represents a printer for outputting an accountant report into the screen
 * Date: 27/8/23 - 19:01
 */
class ScreenPrinter(private val report: AccountantReport) {
  fun print() {
    println("* Año: ${report.year}");
    val endingValuation = report.valuationAtEnd();

    val records = report.records();
    for (record in records) {
      val tipoOperacion = when (record.operation.type) {
        OperationType.TRANSFER -> "TRANSFERENCIA"
        OperationType.WITHDRAW -> "RETIRO"
        OperationType.DEPOSIT -> "DEPOSITO"
        OperationType.COMISION -> "COMISION"
        OperationType.INTEREST -> "INTERESES"
        OperationType.SELL -> "VENTA"
        OperationType.BUY -> "COMPRA"
        else -> {
          throw Error("Tipo de operacion no conocida: " + record.operation.type)
        }
      }
      println("- ${record.operation.moment} $tipoOperacion ${record.operation.exchange.asset} @ ${record.operation.value()}")
      val operationChanges = record.changes
      for (change in operationChanges) {
        println("\t- ${change}")
        println("\t- ${changeSymbolFor(change.value().amount)} ${change.value()} [${change.replacement().asset()} @ ${change.replacement().value()} desde ${change.replaced().value()} en ...")
      }
    }
    val totalProfitOrLoss = endingValuation.totalProfitOrLoss()
    println("Resultado final: ${changeSymbolFor(totalProfitOrLoss.amount)} $totalProfitOrLoss")

    val startingValuation = report.valuationAtStart();
    println("Assets iniciales:${startingValuation.balances()} y finales: ${endingValuation.balances()}")
  }
}
