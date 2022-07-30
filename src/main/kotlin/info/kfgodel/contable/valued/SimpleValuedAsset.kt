package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude

/**
 * This class represents a valued asset without history from where it comes from
 * Date: 30/7/22 - 11:47
 */
data class SimpleValuedAsset(private val asset: Magnitude, private val value: Magnitude) : ValuedAsset {
    override fun asset(): Magnitude {
        return asset
    }

    override fun value(): Magnitude {
        return value
    }

    override fun toString(): String {
        return "$asset = $value"
    }
}