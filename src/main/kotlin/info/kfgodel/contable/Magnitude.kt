package info.kfgodel.contable

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * This type represents a magnitude of a given unit
 * Date: 26/6/22 - 21:30
 */
data class Magnitude(val amount: BigDecimal, val unit:String) {
    fun at(other: Magnitude): Exchange = Exchange(this, other)

    fun negative(): Magnitude = Magnitude(this.amount.negate(), this.unit)
    fun sum(other: Magnitude): Magnitude = Magnitude(this.amount.add(other.amount), unit)
    fun minus(other: Magnitude): Magnitude = Magnitude(this.amount.subtract(other.amount), unit)

    override fun toString(): String {
        return "$amount $unit"
    }

    fun hasSameSignumAs(other: Magnitude): Boolean {
        return this.amount.signum() == other.amount.signum()
    }

    fun isZero(): Boolean {
        return this.amount.signum() == 0 // Fastest way to check for 0 with BigDecimals
    }

    fun isBiggerThan(other: Magnitude): Boolean {
        return this.amount.abs() > other.amount.abs()
    }

  fun exchanged(): Exchange {
    return this.at(this)
  }

  companion object {
        val ROUNDING = RoundingMode.HALF_EVEN
        val MATH_CTX = MathContext(16, ROUNDING)
    }
}


