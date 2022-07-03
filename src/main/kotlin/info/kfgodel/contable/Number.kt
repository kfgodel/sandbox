package info.kfgodel.contable

import java.math.BigDecimal

/**
 * Extension methods for numbers
 * Date: 26/6/22 - 21:28
 */
fun Number.of(assetName: String): Magnitude {
    return Magnitude(BigDecimal(this.toString()), assetName)
}