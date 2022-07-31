package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude

/**
 * This class represents a change in the value of assets
 * Date: 31/7/22 - 12:14
 */
class ValueChange(private val previous: ValuedAsset, private val replacement: ValuedAsset) : ValuedAsset {
    override fun asset(): Magnitude {
        return previous.asset()
    }

    override fun value(): Magnitude {
        val replacedProportion = replacement.proportionalTo(asset().amount)
        val replacingValue = replacedProportion.value().negative() // Replacement always negates previous value signum
        val previousValue = previous.value()
        val valueDifference = replacingValue.minus(previousValue)
        return valueDifference
    }

    override fun equals(other: Any?): Boolean {
        return this.isEqualTo(other)
    }

    override fun hashCode(): Int {
        return this.myHash()
    }

    override fun toString(): String {
        val changeSymbol = when(value().amount.signum()){
            1 -> "^"
            -1 -> "v"
            else -> "="
        }
        return "${asset()} $changeSymbol ${value()}"
    }

    fun isZero(): Boolean {
        return value().isZero()
    }


}