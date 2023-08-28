package info.kfgodel.contable.operations

import info.kfgodel.contable.Exchange
import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.valued.ValuedAsset
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * This type represents an asset operation (increase, decrease, sell, buy, exchange assets)
 * Date: 26/6/22 - 22:53
 */
data class Operation(
  val type: OperationType,
  val exchange: Exchange,
  val moment: LocalDateTime,
  val mainAccount: String = UNDEFINED_ACCOUNT,
  val externalAccount: String = UNDEFINED_ACCOUNT
) : ValuedAsset {
  fun wasDoneBy(date: LocalDateTime) = moment.isEqual(date) || moment.isBefore(date)

  override fun asset(): Magnitude {
    return type.applySignTo(exchange.asset)
  }

  override fun value(): Magnitude {
    return exchange.price
  }

  override fun equals(other: Any?): Boolean {
    return this.isEqualTo(other)
  }

  override fun hashCode(): Int {
    return this.myHash()
  }

  override fun toString(): String {
    val usingAccount = mainAccount.let { acc -> if(acc == UNDEFINED_ACCOUNT) "" else " using $acc" }
    val andAccount = externalAccount.let { acc -> if(acc == UNDEFINED_ACCOUNT) "" else " and $acc" }
    return "$type $exchange on $moment$usingAccount$andAccount"
  }

  fun using(accountName: String): Operation {
    return Operation(type, exchange, moment, accountName)
  }

  fun and(accountName: String): Operation {
    return Operation(type, exchange, moment, mainAccount, accountName)
  }

  fun splitBy(splitAmount: BigDecimal): Pair<Operation, Operation> {
    val splitOperation = Operation(type,exchange.proportionalto(splitAmount),moment,mainAccount,externalAccount)
    val remainingAmount = exchange.asset().amount.minus(splitAmount)
    val remainingOperation = Operation(type,exchange.proportionalto(remainingAmount),moment,mainAccount,externalAccount)
    return Pair(splitOperation, remainingOperation)
  }

  companion object {
    const val UNDEFINED_ACCOUNT = "UNDEFINED"
  }

}
