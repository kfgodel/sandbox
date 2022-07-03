package info.kfgodel.contable

import info.kfgodel.contable.operations.Operation
import info.kfgodel.contable.operations.OperationType
import java.time.LocalDateTime
import java.util.TreeMap

/**
 * This class represents a repository for assets
 * Date: 26/6/22 - 21:10
 */
class AssetHolder {

    private val operations = TreeMap<LocalDateTime, Operation>()

    fun assets(): Set<Magnitude> {
        val sums = TreeMap<String, Magnitude>()
        operations.values.forEach { operation ->
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

    fun acquired(exchange: Exchange, moment: LocalDateTime) {
        register(Operation(OperationType.BUY, exchange, moment))
    }

    fun sold(exchange: Exchange, moment: LocalDateTime) {
        register(Operation(OperationType.SELL, exchange, moment))
    }

    private fun register(operation: Operation) {
        operations[operation.moment] = operation
    }
}