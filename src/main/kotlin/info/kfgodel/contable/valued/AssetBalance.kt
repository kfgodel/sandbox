package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.of
import java.util.LinkedList

/**
 * This type represents the total balance of a given asset
 *
 * Date: 30/7/22 - 14:35
 */
class AssetBalance(val assetUnit:String, val valueUnit:String): ValuedAsset {
    private val values: LinkedList<ValuedAsset> = LinkedList()

    fun valuables(): LinkedList<ValuedAsset> {
        return values
    }

    override fun asset(): Magnitude {
        if(values.isEmpty()){
            return 0.of(assetUnit)
        }
        return values
            .map(ValuedAsset::asset)
            .reduce(Magnitude::sum)
    }

    override fun value(): Magnitude {
        if(values.isEmpty()){
            return 0.of(valueUnit)
        }
        return values
            .map(ValuedAsset::value)
            .reduce(Magnitude::sum)
    }

    fun updateWith(latest: ValuedAsset) : List<ValuedAsset> {
        validateUpdatedAsset(latest)
        val consumed = mutableListOf<ValuedAsset>()
        var latestAsset = latest.asset()
        while (!latestAsset.isZero() && !values.isEmpty()){ // While we have an amount to update and values to consume
            val oldest = values.removeFirst()
            val oldestAsset = oldest.asset()
            val remainingAsset = oldestAsset.sum(latestAsset)
            if(remainingAsset.hasSameSignumAs(oldestAsset)){
                // We didn't consume all the oldest asset
                if(remainingAsset.isBiggerThan(oldestAsset)){
                    // It's an increase of assets not a consumption. We keep both
                    values.addFirst(oldest)
                    values.addLast(latest)
                }else{
                    // We keep only what wasn't consumed from the oldest
                    val updatedOldest = oldest.proportionalTo(remainingAsset.amount)
                    values.addFirst(updatedOldest)
                    val partiallyConsumed = oldest.asset().minus(remainingAsset)
                    val consumedValue = oldest.proportionalTo(partiallyConsumed.amount)
                    consumed.add(consumedValue)
                }
                latestAsset = 0.of(assetUnit)
            }else{
                // We consumed all the oldest asset, but there's still more to be updated
                consumed.add(oldest)
                latestAsset = remainingAsset
            }
        }
        if(!latestAsset.isZero()){
            // We still have an amount to update after consuming all pre-existing values. This changes balance signum
            val newValue = if (latestAsset == latest.asset()) {
                latest
            } else {
                latest.proportionalTo(latestAsset.amount)
            }
            values.addFirst(newValue)
        }
        return consumed
    }

    private fun validateUpdatedAsset(valued: ValuedAsset) {
        val updatedAssetUnit = valued.asset().unit
        if (updatedAssetUnit != assetUnit) {
            throw UnsupportedOperationException("Update using a different asset[${updatedAssetUnit}] than expected[${assetUnit}]")
        }
        val updatedValueUnit = valued.value().unit
        if (updatedValueUnit != valueUnit) {
            throw UnsupportedOperationException("Update using a different value[${updatedValueUnit}] than expected[${valueUnit}]")
        }
    }

    override fun equals(other: Any?): Boolean {
        return this.isEqualTo(other)
    }

    override fun hashCode(): Int {
        return this.myHash()
    }

    override fun toString(): String {
        return "${asset()} = ${value()} from ${values.size} values"
    }


}