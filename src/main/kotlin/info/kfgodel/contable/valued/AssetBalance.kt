package info.kfgodel.contable.valued

import info.kfgodel.contable.accountant.PortfolioOperation
import info.kfgodel.contable.calculator.ChangeCalculator
import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.of
import java.util.LinkedList

/**
 * This type represents the total balance of a given asset
 *
 * Date: 30/7/22 - 14:35
 */
class AssetBalance(val assetUnit: String, val valueUnit: String) {
  private var operations: List<PortfolioOperation> = LinkedList()

  fun valuables(): List<PortfolioOperation> {
    return operations
  }

  fun asset(): Magnitude {
    if (operations.isEmpty()) {
      return 0.of(assetUnit)
    }
    return operations
      .map { it.asset() }
      .reduce(Magnitude::sum)
  }

  fun value(): Magnitude {
    if (operations.isEmpty()) {
      return 0.of(valueUnit)
    }
    return operations
      .map { it.value() }
      .reduce(Magnitude::sum)
  }

  fun updateWith(newOperation: PortfolioOperation): List<ValueChange<PortfolioOperation>> {
    validateSameAsset(newOperation)
    val result = ChangeCalculator(operations)
      .calculateFor(newOperation)
    this.operations = result.remainingOperations()
    val valueChanges = result.changes()
      .map { change -> ValueChange(change.first, change.second) }
      .filter { change -> !change.isZero() }

    if (valueChanges.isEmpty() || valueChanges.map { change -> change.value() }.reduce(Magnitude::sum).isZero()) {
      // All the value changes cancel each other. It's not really a value change
      return emptyList()
    }
    return valueChanges
  }

  private fun validateSameAsset(newOperation: PortfolioOperation) {
    val newUnit = newOperation.asset().unit
    if (newUnit != assetUnit) {
      throw UnsupportedOperationException("Update using a different asset[${newUnit}] than expected[${assetUnit}]")
    }
  }

  override fun equals(other: Any?): Boolean {
    return isEqualTo(other, asset(), value())
  }

  override fun hashCode(): Int {
    return hashOf(asset(), value())
  }

  override fun toString(): String {
    return "${asset()} @ ${value()} (${operations.size} ops)"
  }


}