package info.kfgodel.contable

import info.kfgodel.contable.valued.ValuedAsset

/**
 * This type reprents an exchange of two assets which implies a exchange rate
 * Date: 26/6/22 - 22:00
 */
data class Exchange(val asset: Magnitude, val price: Magnitude) : ValuedAsset {
    override fun toString(): String {
        return "$asset @ $price"
    }

    override fun asset(): Magnitude {
        return asset
    }

    override fun value(): Magnitude {
        return price
    }
}
