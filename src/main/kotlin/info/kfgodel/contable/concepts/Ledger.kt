package info.kfgodel.contable.concepts

import info.kfgodel.contable.accountant.AccountantReport
import info.kfgodel.contable.accountant.PortfolioValuation
import info.kfgodel.contable.reader.OperationsReader
import java.io.File
import java.time.LocalDateTime
import java.util.TreeMap

/**
 * This class represents a repository for assets
 * Date: 26/6/22 - 21:10
 */
class Ledger {

  private val operations = TreeMap<LocalDateTime, MutableList<Operation>>()

  fun operations() = operations.values.flatten()

  fun register(operation: Operation) {
    operations.computeIfAbsent(operation.moment) { _ -> mutableListOf() }.add(operation)
  }

  fun valuation(referenceDate: LocalDateTime, valueUnit: String): PortfolioValuation {
    val valuation = PortfolioValuation(valueUnit)
    operationsBy(referenceDate).forEach { operation ->
      valuation.include(operation)
    }
    return valuation
  }

  private fun operationsBy(date: LocalDateTime) = operations.values
    .flatten()
    .filter { operation -> operation.wasDoneBy(date) }

  fun operationsMatching(condition: (Operation) -> Boolean): List<Operation> {
    return operations().filter(condition)
  }

  fun reportFor(year: Int, valueUnit: String): AccountantReport {
    return AccountantReport(this, year, valueUnit)
  }

  fun fromFile(fileName: String, fileReader: OperationsReader): Ledger {
    val reportFile = File(this.javaClass.classLoader.getResource(fileName).file)
    fileReader.addReportFile(reportFile).operations().forEach(this::register)
    return this
  }

}