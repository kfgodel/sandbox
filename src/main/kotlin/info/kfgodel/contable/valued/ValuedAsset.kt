package info.kfgodel.contable.valued

import info.kfgodel.contable.concepts.Magnitude
import java.math.BigDecimal
import java.util.Objects

/**
 * This class represents an amount of asset valued at a point in time
 * Date: 28/7/22 - 22:37
 */
interface ValuedAsset<S: ValuedAsset<S>> {
  fun asset(): Magnitude
  fun value(): Magnitude
  fun splitBy(splitAmount: BigDecimal): Pair<S, S>
  fun hasAsset(): Boolean {
    return !asset().isZero()
  }
}

fun isEqualTo(other: Any?, asset: Magnitude, value: Magnitude): Boolean {
  if (other !is ValuedAsset<*>) {
    return false
  }
  return asset == other.asset() && value == other.value()
}

fun hashOf(asset: Magnitude, value: Magnitude): Int {
  return Objects.hash(asset, value)
}

