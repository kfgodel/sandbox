package info.kfgodel.contable.concepts

import java.time.LocalDateTime

/**
 * Thie enum defines the types of operation that can be registered
 * Date: 26/6/22 - 23:15
 */
enum class OperationType {
  BUY{
    override fun done(moment: LocalDateTime, exchange: Exchange): Operation {
      return super.done(moment, exchange.asset.at(exchange.value().negative()))
    }
  },
  SELL{
    override fun done(moment: LocalDateTime, exchange: Exchange): Operation {
      return super.done(moment, exchange.asset.negative().at(exchange.value()))
    }
  },
  DEPOSIT,
  WITHDRAW,
  TRANSFER,
  INTEREST,
  COMISION;

  open fun done(moment: LocalDateTime, exchange: Exchange): Operation {
    return Operation(this, exchange, moment)
  }

}
