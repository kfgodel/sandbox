package info.kfgodel.contable.operations

import info.kfgodel.contable.Exchange
import info.kfgodel.contable.Magnitude

/**
 * Thie enum defines the types of operation that can be registered
 * Date: 26/6/22 - 23:15
 */
enum class OperationType {
    BUY {
        override fun gainedIn(exchange: Exchange): Magnitude = exchange.first
        override fun lostIn(exchange: Exchange): Magnitude = exchange.second
    },
    SELL {
        override fun gainedIn(exchange: Exchange): Magnitude = exchange.second
        override fun lostIn(exchange: Exchange): Magnitude = exchange.first
    };

    abstract fun gainedIn(exchange: Exchange): Magnitude
    abstract fun lostIn(exchange: Exchange): Magnitude
}