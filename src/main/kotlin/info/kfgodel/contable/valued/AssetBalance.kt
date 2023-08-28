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
      var currentOperation = newOperation
      while (!currentOperation.asset().isZero() && operations.isNotEmpty()){ // While we have an amount to update and values to consume
        val oldestOperation = operations.removeFirst()
        val oldestAsset = oldestOperation.asset()
        val currentAsset = currentOperation.asset()
        if(currentAsset.hasSameSignumAs(oldestAsset)){
          // Both operations go in the same direction without consuming each other. We keep both
          val (consumed, zero) = currentOperation.splitBy(currentAsset.amount.abs())
          operations.addFirst(oldestOperation)
          operations.addLast(consumed)
          currentOperation = zero // nothing left to distribute
        }else{
          val (consumedOldest, remainingOldest) = oldestOperation.splitBy(currentAsset.amount.abs())
          val (consumedCurrent, remainingCurrent) = currentOperation.splitBy(oldestAsset.amount.abs())
          changes.add(ValueChange(consumedOldest, consumedCurrent))
          if(!remainingOldest.asset().isZero()){
            // The oldest operation still has remaining value to use in next update
            operations.addFirst(remainingOldest)
          }
          currentOperation = remainingCurrent
        }
      }
      if(!currentOperation.asset().isZero()){
          // We still have an amount to update after consuming all pre-existing values. This changes balance signum
          operations.addFirst(currentOperation)
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