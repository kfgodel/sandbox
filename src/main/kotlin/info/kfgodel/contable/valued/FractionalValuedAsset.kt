package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.of
import java.math.BigDecimal

/**
 * This class represents a fraction of a valued asset
 * Date: 30/7/22 - 20:13
 */
class FractionalValuedAsset(val assetFraction: BigDecimal, private val original: ValuedAsset ) : ValuedAsset {
    override fun asset(): Magnitude {
        return assetFraction.of(originalAsset().unit)
    }

    override fun value(): Magnitude {
        val proportionalValue = assetFraction.multiply(originalValue().amount).divide(originalAsset().amount, Magnitude.MATH_CTX)
        return proportionalValue.of(originalValue().unit)
    }

    private fun originalAsset() = original.asset()
    private fun originalValue() = original.value()

    override fun proportionalTo(fraction: BigDecimal): ValuedAsset {
        return FractionalValuedAsset(fraction, original)
    }

    override fun equals(other: Any?): Boolean {
        return this.isEqualTo(other)
    }

    override fun hashCode(): Int {
        return this.myHash()
    }

    override fun toString(): String {
        return "${asset()} = ${value()} from ${original}"
    }

}