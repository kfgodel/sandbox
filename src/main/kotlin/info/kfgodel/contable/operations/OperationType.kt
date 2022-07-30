package info.kfgodel.contable.operations

import info.kfgodel.contable.Exchange
import info.kfgodel.contable.Magnitude
import java.time.LocalDateTime

/**
 * Thie enum defines the types of operation that can be registered
 * Date: 26/6/22 - 23:15
 */
enum class OperationType {
    BUY {
        override fun gainedIn(exchange: Exchange): Magnitude = exchange.asset
        override fun lostIn(exchange: Exchange): Magnitude = exchange.price
        override fun applySignTo(magnitude: Magnitude): Magnitude = magnitude
    },
    SELL {
        override fun gainedIn(exchange: Exchange): Magnitude = exchange.price
        override fun lostIn(exchange: Exchange): Magnitude = exchange.asset
        override fun applySignTo(magnitude: Magnitude): Magnitude = magnitude.negative()
    };

    abstract fun gainedIn(exchange: Exchange): Magnitude
    abstract fun lostIn(exchange: Exchange): Magnitude
    abstract fun applySignTo(magnitude: Magnitude): Magnitude
    fun done(moment: LocalDateTime, exchange: Exchange): Operation {
        return Operation(this, exchange, moment)
    }

}