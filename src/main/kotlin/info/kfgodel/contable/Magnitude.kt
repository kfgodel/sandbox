package info.kfgodel.contable

import java.math.BigDecimal

/**
 * This type represents a magnitude of a given unit
 * Date: 26/6/22 - 21:30
 */
data class Magnitude(val amount: BigDecimal, val unit:String) {
    fun at(other: Magnitude): Exchange = Exchange(this, other)

    fun negative(): Magnitude = Magnitude(this.amount.negate(), this.unit)
    fun sum(other: Magnitude): Magnitude = Magnitude(this.amount.add(other.amount), unit)
    fun minus(other: Magnitude): Magnitude = Magnitude(this.amount.subtract(other.amount), unit)
}


