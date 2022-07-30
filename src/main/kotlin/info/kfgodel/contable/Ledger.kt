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

    fun assets(date: LocalDateTime): Set<Magnitude> {
        val sums = TreeMap<String, Magnitude>()
        operationsBy(date)
            .forEach { operation ->
            val gained = operation.gained()
            sums.compute(gained.unit) { _, total ->
                total?.sum(gained) ?: gained
            }
            val lost = operation.lost()
            sums.compute(lost.unit) { _, total ->
                total?.minus(lost) ?: lost.negative()
            }
        }
        return sums.values.toSet()
    }

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

}