package info.kfgodel.contable.valued

import info.kfgodel.contable.Magnitude
import info.kfgodel.contable.of
import info.kfgodel.contable.operations.Operation
import java.util.LinkedList

/**
 * This type represents the total balance of a given asset
 *
 * Date: 30/7/22 - 14:35
 */
class AssetBalance(val assetUnit:String, val valueUnit:String): ValuedAsset {
    private val operations: LinkedList<Operation> = LinkedList()

    fun valuables(): List<Operation> {
        return operations
    }

    override fun asset(): Magnitude {
        if(operations.isEmpty()){
            return 0.of(assetUnit)
        }
        return operations
            .map(ValuedAsset::asset)
            .reduce(Magnitude::sum)
    }

    override fun value(): Magnitude {
        if(operations.isEmpty()){
            return 0.of(valueUnit)
        }
        return operations
            .map(ValuedAsset::value)
            .reduce(Magnitude::sum)
    }

    fun updateWith(newOperation: Operation) : List<ValueChange> {
        validateUpdatedAsset(newOperation)
        val changes = mutableListOf<ValueChange>()
        var newAsset = newOperation.asset()
        while (!newAsset.isZero() && operations.isNotEmpty()){ // While we have an amount to update and values to consume
            val oldestOperation = operations.removeFirst()
            val oldestAsset = oldestOperation.asset()
            val resultingAsset = oldestAsset.sum(newAsset)
            if(resultingAsset.hasSameSignumAs(oldestAsset)){
                // We didn't consume all the oldest asset
                if(resultingAsset.isBiggerThan(oldestAsset)){
                    // It's an increase of assets not a consumption. We keep both
                    operations.addFirst(oldestOperation)
                    operations.addLast(newOperation)
                }else{
                    // We keep only what wasn't consumed from the oldest
                  val (notConsumed, consumed) = oldestOperation.splitBy(resultingAsset.amount.abs())
                  operations.addFirst(notConsumed)
                  val (consumer,_) = newOperation.splitBy(newAsset.amount.abs())
                  changes.add(ValueChange(consumed, consumer))
                }
                newAsset = 0.of(assetUnit)
            }else{
              // We consumed all the oldest asset, but there may be more to be updated
              val (consumed, notConsumed) = newOperation.splitBy(oldestAsset.amount.abs())
              changes.add(ValueChange(oldestOperation, consumed)) // Nothing left on the new asset either
              newAsset = resultingAsset
            }
        }
        if(!newAsset.isZero()){
            // We still have an amount to update after consuming all pre-existing values. This changes balance signum
            val newValue = if (newAsset == newOperation.asset()) {
                newOperation // Everything is still un-consumed
            } else {
              val (notConsumed, _) = newOperation.splitBy(newAsset.amount.abs())
              notConsumed // Only what was not consumed remains
            }
            operations.addFirst(newValue)
        }
        return changes
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
        return "${asset()} @ ${value()} from ${operations.size} values"
    }


}