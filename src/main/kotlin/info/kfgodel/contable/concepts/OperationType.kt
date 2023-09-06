package info.kfgodel.contable.concepts

import java.time.LocalDateTime

/**
 * Thie enum defines the types of operation that can be registered
 * Date: 26/6/22 - 23:15
 */
enum class OperationType {
  BUY,
  SELL,
  DEPOSIT,
  WITHDRAW,
  TRANSFER,
  INTEREST,
  COMISION;

  fun done(moment: LocalDateTime, exchange: Exchange): Operation {
    return Operation(this, exchange, moment)
  }

}
