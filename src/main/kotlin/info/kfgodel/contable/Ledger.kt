package info.kfgodel.contable

import info.kfgodel.contable.operations.Operation
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
            valuation.include(operation.valued())
        }
        return valuation
    }

    private fun operationsBy(date: LocalDateTime) = operations.values
        .flatten()
        .filter { operation -> operation.wasDoneBy(date) }

    fun operationsMatching(condition: (Operation) -> Boolean): List<Operation> {
        return operations().filter(condition)
    }

}