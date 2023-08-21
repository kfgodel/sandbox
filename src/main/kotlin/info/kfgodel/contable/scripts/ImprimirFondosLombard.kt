package info.kfgodel.contable.scripts

import info.kfgodel.contable.LombardFund
import info.kfgodel.contable.operations.OperationType
import info.kfgodel.contable.valued.changeSymbolFor

fun main() {
    for (year in 2019..2023) {
        val report = LombardFund().reportFor(year)
        println("* Año: ${report.year}");

      // Llamamos para que se generen los registros... mala solucion
      val endingValuation = report.valuationAtEnd();

      val records = report.records();
      for (record in records) {
        val tipoOperacion = if(record.operation.type == OperationType.BUY) "SUSCRIPCION" else "RESCATE"
        println("- ${record.operation.moment} $tipoOperacion ${record.operation.exchange.asset} @ ${record.operation.value()}")
        val operationChanges = record.changes
        for (change in operationChanges) {
          println("\t- $change")
        }
      }
      val totalProfitOrLoss = endingValuation.totalProfitOrLoss()
      println("Resultado final: ${changeSymbolFor(totalProfitOrLoss.amount)} $totalProfitOrLoss")

      val startingValuation = report.valuationAtStart();
      println("Assets iniciales:${startingValuation.balances()} y finales: ${endingValuation.balances()}")
    }
}
