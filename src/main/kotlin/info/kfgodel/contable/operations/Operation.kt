package info.kfgodel.contable.operations

import info.kfgodel.contable.Exchange
import info.kfgodel.contable.Magnitude
import java.time.LocalDateTime

/**
 * This type represents a buy operation (from the buyer perspective)
 * Date: 26/6/22 - 22:53
 */
data class Operation(val type: OperationType, val exchange: Exchange, val moment: LocalDateTime) {
    fun gained(): Magnitude = type.gainedIn(exchange)
    fun lost(): Magnitude = type.lostIn(exchange)
}