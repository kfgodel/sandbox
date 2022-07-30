package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.of
import java.math.BigDecimal
import java.util.LinkedList

/**
 * This type represents the total balance of a given asset
 *
 * Date: 30/7/22 - 14:35
 */
class AssetBalance(val assetUnit:String, val valueUnit:String) {
    private val values: LinkedList<ValuedAsset> = LinkedList()

    fun values(): LinkedList<ValuedAsset> {
        return values
    }

    fun asset(): Magnitude {
        if(values.isEmpty()){
            return 0.of(assetUnit)
        }
        return values
            .map(ValuedAsset::asset)
            .reduce(Magnitude::sum)
    }

    fun value(): Magnitude {
        if(values.isEmpty()){
            return 0.of(valueUnit)
        }
        return values
            .map(ValuedAsset::value)
            .reduce(Magnitude::sum)
    }

    fun updateWith(valued: ValuedAsset) {
        validateUpdatedAsset(valued)
        var amountToUpdate = valued.asset().amount
        while (!values.isEmpty()){
            val oldest = values.removeFirst()
            val oldestAvailable = oldest.asset().amount
            if(oldestAvailable.signum() == amountToUpdate.signum()){
                // It's an increase of assets amount (either positive or negative increase). We keep both
                values.addFirst(oldest)
                values.addLast(valued)
                amountToUpdate = BigDecimal.ZERO
                break
            }
            // It's a reduction of asset, we need to update remaining amounts
            val oldestRemaining = oldestAvailable.add(amountToUpdate)
            if(oldestRemaining.signum() == 0){
                // We used all the available asset with the oldest value. Nothing else to do
                amountToUpdate = BigDecimal.ZERO
                break
            }else if(oldestRemaining.signum() == oldestAvailable.signum()){
                // There's still some remaining asset from the oldest
                val valuedRemaining = oldest.proportionalTo(oldestRemaining)
                values.addFirst(valuedRemaining)
                amountToUpdate = BigDecimal.ZERO
                break
            }else {
                // We used all the available from oldest but there's still more to reduce
                amountToUpdate = oldestRemaining
            }
        }
        if(amountToUpdate.signum() != 0){
            // There are no remaining values but we still have an amount to update. This changes balance signum
            var newValue = valued
            if(!amountToUpdate.equals(valued.asset().amount)){
                newValue = valued.proportionalTo(amountToUpdate)
            }
            values.addFirst(newValue)
        }
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

}