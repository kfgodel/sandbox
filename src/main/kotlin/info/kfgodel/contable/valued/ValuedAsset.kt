package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.of
import java.math.BigDecimal

/**
 * This class represents an amount of asset valued at a point in time
 * Date: 28/7/22 - 22:37
 */
interface ValuedAsset{
    fun asset(): Magnitude
    fun value(): Magnitude
    fun proportionalTo(remaining: BigDecimal): ValuedAsset {
     val proportionalValue = remaining.multiply(value().amount).divide(asset().amount, Magnitude.MATH_CTX)
     return SimpleValuedAsset(remaining.of(asset().unit), proportionalValue.of(value().unit))
    }
}