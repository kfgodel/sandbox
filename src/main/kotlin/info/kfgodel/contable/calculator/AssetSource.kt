package info.kfgodel.contable.calculator

import info.kfgodel.contable.concepts.Magnitude
import java.math.BigDecimal

/**
 * This type represents a generic source of asset that can be used to make abstract asset calculations
 * without knowing the specific of the source
 * Date: 8/10/23 - 20:32
 */
interface AssetSource<A: AssetSource<A>> {
  fun asset(): Magnitude
  fun splitBy(splitAmount: BigDecimal): Pair<A, A>
  fun hasAsset(): Boolean {
    return !asset().isZero()
  }
}