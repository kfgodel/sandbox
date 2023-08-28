package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.operations.Operation
import java.math.BigDecimal

/**
 * This class represents a change in the value of assets
 * Date: 31/7/22 - 12:14
 */
class ValueChange(private val previous: Operation, private val next: Operation) : ValuedAsset {
    override fun asset(): Magnitude {
        return previous.asset()
    }

    override fun value(): Magnitude {
        val replacingValue = replacement().value() // Replacement always negates previous value signum
        val previousValue = replaced().value()
        val valueDifference = replacingValue.minus(previousValue)
        return valueDifference
    }

    fun replaced(): Operation {
        return previous
    }

    fun replacement(): Operation {
      return next
    }

    override fun equals(other: Any?): Boolean {
        return this.isEqualTo(other)
    }

    override fun hashCode(): Int {
        return this.myHash()
    }

    override fun toString(): String {
      val symbol = changeSymbolFor(value().amount)
      return "$symbol ${value()} [${replacement()}, ${replaced()}]"
    }

  fun isZero(): Boolean {
        return value().isZero()
    }
}

fun changeSymbolFor(amount: BigDecimal): String {
  val changeSymbol = when (amount.signum()) {
    1 -> "↑"
    -1 -> "↓"
    else -> "≔"
  }
  return changeSymbol
}

