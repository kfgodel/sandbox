package info.kfgodel.contable.operations

import info.kfgodel.contable.Exchange
import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.valued.ValuedAsset
import java.time.LocalDateTime

/**
 * This type represents a buy operation (from the buyer perspective)
 * Date: 26/6/22 - 22:53
 */
data class Operation(val type: OperationType, val exchange: Exchange, val moment: LocalDateTime) : ValuedAsset {
    fun gained(): Magnitude = type.gainedIn(exchange)
    fun lost(): Magnitude = type.lostIn(exchange)
    fun wasDoneBy(date: LocalDateTime) = moment.isEqual(date) || moment.isBefore(date)
    fun valued(): ValuedAsset {
        return this
    }

    override fun asset(): Magnitude {
        return type.applySignTo(exchange.asset)
    }

    override fun value(): Magnitude {
        return type.applySignTo(exchange.price)
    }

    override fun equals(other: Any?): Boolean {
        return this.isEqualTo(other)
    }

    override fun hashCode(): Int {
        return this.myHash()
    }
}