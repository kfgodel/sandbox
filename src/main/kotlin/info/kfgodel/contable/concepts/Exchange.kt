package info.kfgodel.contable.concepts

import info.kfgodel.contable.of
import info.kfgodel.contable.valued.ValuedAsset
import java.math.BigDecimal

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

  fun proportionalto(newAssetAmount: BigDecimal): Exchange {
    val newValueAmount = value().amount.multiply(newAssetAmount)
      .divide(asset().amount, Magnitude.MATH_CTX)
    return newAssetAmount.of(asset().unit).at(newValueAmount.of(value().unit))
  }

  fun inverse(): Exchange {
    return value().at(asset())
  }
}
