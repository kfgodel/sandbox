package info.kfgodel.contable.valued

import info.kfgodel.contable.calculator.ChangeCalculator
import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.of
import java.util.LinkedList

/**
 * This type represents the total balance of a given asset
 *
 * Date: 30/7/22 - 14:35
 */
class AssetBalance(val assetUnit:String, val valueUnit:String): ValuedAsset {
    private var operations: List<Operation> = LinkedList()

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
      validateSameAsset(newOperation)
      val result = ChangeCalculator(operations)
        .calculateFor(newOperation)
      this.operations = result.remainingOperations()
      return result.valueChanges()
    }

  private fun validateSameAsset(newOperation: Operation) {
    val newUnit = newOperation.asset().unit
    if(newUnit != assetUnit){
      throw UnsupportedOperationException("Update using a different asset[${newUnit}] than expected[${assetUnit}]")
    }
  }

    override fun equals(other: Any?): Boolean {
        return this.isEqualTo(other)
    }

    override fun hashCode(): Int {
        return this.myHash()
    }

    override fun toString(): String {
        return "${asset()} @ ${value()} (${operations.size} ops)"
    }


}