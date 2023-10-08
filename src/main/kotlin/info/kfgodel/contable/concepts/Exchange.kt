package info.kfgodel.contable.concepts

import info.kfgodel.contable.of
import java.math.BigDecimal

/**
 * This type reprents an exchange of two assets which implies a exchange rate
 * Date: 26/6/22 - 22:00
 */
data class Exchange(val asset: Magnitude, val price: Magnitude) {
    override fun toString(): String {
        return "$asset @ $price"
    }

    fun asset(): Magnitude {
        return asset
    }

    fun value(): Magnitude {
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
