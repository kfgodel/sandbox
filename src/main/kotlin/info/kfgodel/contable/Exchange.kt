package info.kfgodel.contable

import info.kfgodel.contable.valued.ValuedAsset

/**
 * This type reprents an exchange of two assets which implies a exchange rate
 * Date: 26/6/22 - 22:00
 */
data class Exchange(val first: Magnitude, val second: Magnitude) : ValuedAsset {
    override fun toString(): String {
        return "$first x $second"
    }

    override fun asset(): Magnitude {
        return first
    }

    override fun value(): Magnitude {
        return second
    }
}