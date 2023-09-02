package info.kfgodel.contable.concepts

import info.kfgodel.contable.concepts.Exchange
import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
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
  },
  DEPOSIT {
    override fun gainedIn(exchange: Exchange): Magnitude = exchange.price
    override fun lostIn(exchange: Exchange): Magnitude = exchange.asset
    override fun applySignTo(magnitude: Magnitude): Magnitude = magnitude.negative()
  },
  WITHDRAW {
    override fun gainedIn(exchange: Exchange): Magnitude = exchange.asset
    override fun lostIn(exchange: Exchange): Magnitude = exchange.price
    override fun applySignTo(magnitude: Magnitude): Magnitude = magnitude
  },
  TRANSFER {
    override fun gainedIn(exchange: Exchange): Magnitude = exchange.price
    override fun lostIn(exchange: Exchange): Magnitude = exchange.asset
    override fun applySignTo(magnitude: Magnitude): Magnitude = magnitude.negative()
  },
  INTEREST {
    override fun gainedIn(exchange: Exchange): Magnitude = exchange.asset
    override fun lostIn(exchange: Exchange): Magnitude = exchange.price
    override fun applySignTo(magnitude: Magnitude): Magnitude = magnitude
  },
  COMISION {
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
