package info.kfgodel.contable

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Extension methods for numbers
 * Date: 26/6/22 - 21:28
 */
fun Number.of(assetName: String): Magnitude {
    return Magnitude(BigDecimal(this.toString(), Magnitude.MATH_CTX).setScale(2, RoundingMode.HALF_EVEN), assetName)
}