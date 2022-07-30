package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.of
import java.math.BigDecimal
import java.util.Objects

/**
 * This class represents an amount of asset valued at a point in time
 * Date: 28/7/22 - 22:37
 */
interface ValuedAsset{
    fun asset(): Magnitude
    fun value(): Magnitude
    fun proportionalTo(fraction: BigDecimal): ValuedAsset {
        val assetAmount = this.asset().amount
        val proportionalValue = fraction.multiply(value().amount).divide(assetAmount, Magnitude.MATH_CTX)
        return SimpleValuedAsset(fraction.of(asset().unit), proportionalValue.of(value().unit))
    }
    fun isEqualTo(other: Any?): Boolean{
        if (other !is ValuedAsset){
            return false
        }
        return this.asset() == other.asset() && this.value() == other.value()
    }
    fun myHash(): Int {
        return Objects.hash(this.asset(), this.value())
    }
}
