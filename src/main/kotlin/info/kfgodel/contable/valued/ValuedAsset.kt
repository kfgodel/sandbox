package info.kfgodel.contable.valued

import info.kfgodel.contable.concepts.Magnitude
import java.util.Objects

/**
 * This class represents an amount of asset valued at a point in time
 * Date: 28/7/22 - 22:37
 */
interface ValuedAsset{
    fun asset(): Magnitude
    fun value(): Magnitude
    fun isEqualTo(other: Any?): Boolean{
        if (other !is ValuedAsset){
            return false
        }
        return this.asset() == other.asset() && this.value() == other.value()
    }
    fun myHash(): Int {
        return Objects.hash(this.asset(), this.value())
    }

  fun hasAsset(): Boolean {
    return !asset().isZero()
  }
}

