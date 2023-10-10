package info.kfgodel.contable.valued

import info.kfgodel.contable.concepts.Magnitude
import java.math.BigDecimal

/**
 * This class represents a change in the value of assets
 * Date: 31/7/22 - 12:14
 */
class ValueChange<S : ValuedAsset<S>>(private val previous: S, private val next: S) {
  fun asset(): Magnitude {
    return previous.asset()
  }

  fun value(): Magnitude {
    val replacingValue = replacement().value()
    val previousValue = replaced().value()
    if (replacingValue.unit != previousValue.unit) {
      throw NotImplementedError("Diferencia de unidad en value change: $previousValue -> $replacingValue");
      // If we have different currencies there's no loss or gain, just exchange
      //return 0.of(replacingValue.unit)
    }
    return replacingValue.minus(previousValue)
  }

  fun replaced(): S {
    return previous
  }

  fun replacement(): S {
    return next
  }

  override fun equals(other: Any?): Boolean {
    return isEqualTo(other, asset(), value())
  }

  override fun hashCode(): Int {
    return hashOf(asset(), value())
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

