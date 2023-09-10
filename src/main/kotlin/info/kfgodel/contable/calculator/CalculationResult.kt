package info.kfgodel.contable.calculator

import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.valued.ValueChange
import java.util.LinkedList

/**
 * This type represents the result of calculating the impact of one operation over others
 */
class CalculationResult(startingOperations: List<Operation>) {
  private val remainingOperations = LinkedList(startingOperations)
  private val changes = mutableListOf<ValueChange>()
  /**
   * State of original operations after the impact. It may result in the loss, gain or any combination of those
   */
  fun remainingOperations(): List<Operation> {
    return remainingOperations
  }

  /**
   * List of changes in the value of original operations after the impact of the new
   */
  fun changes(): List<ValueChange> {
    return changes
  }

  fun hasRemainingAsset(): Boolean {
    return this.remainingOperations().isNotEmpty()
  }

  fun removeOldest(): Operation {
    return remainingOperations.removeFirst();
  }

  fun keepAsOldest(oldestOperation: Operation) {
    remainingOperations.addFirst(oldestOperation)
  }

  fun keepAsNewest(newest: Operation) {
    remainingOperations.addLast(newest)
  }

  fun recordChange(consumedOldest: Operation, consumedCurrent: Operation) {
    changes.add(ValueChange(consumedOldest, consumedCurrent))
  }

}
