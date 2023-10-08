package info.kfgodel.contable.calculator

import info.kfgodel.contable.valued.ValueChange
import info.kfgodel.contable.valued.ValuedAsset
import java.util.LinkedList

/**
 * This type represents the result of calculating the impact of one operation over others
 */
class AssetVariation<S: ValuedAsset<S>>(val startingOperations: List<S>, val modifyingOperation: S) {
  private val remainingOperations = LinkedList(startingOperations)
  private val changes = mutableListOf<ValueChange<S>>()
  private val addedOperations = mutableListOf<S>()
  private val reducedOperations = mutableListOf<S>()

  fun addedOperations(): List<S> {
    return addedOperations;
  }
  fun reducedOperations(): List<S> {
    return reducedOperations;
  }

  /**
   * State of original operations after the impact. It may result in the loss, gain or any combination of those
   */
  fun remainingOperations(): List<S> {
    return remainingOperations
  }

  /**
   * List of changes in the value of original operations after the impact of the new
   */
  fun valueChanges(): List<ValueChange<S>> {
    return changes
  }

  fun hasRemainingAsset(): Boolean {
    return this.remainingOperations().isNotEmpty()
  }

  fun removeOldest(): S {
    return remainingOperations.removeFirst();
  }

  fun keepAsOldest(oldestOperation: S) {
    remainingOperations.addFirst(oldestOperation)
  }

  fun keepAsNewest(newest: S) {
    remainingOperations.addLast(newest)
  }

  fun recordChange(consumedOldest: S, consumedCurrent: S) {
    changes.add(ValueChange(consumedOldest, consumedCurrent))
  }

  fun addOperation(addedOperation: S) {
    addedOperations.add(addedOperation)
  }

  fun reduceOperation(reducedOperation: S) {
    reducedOperations.add(reducedOperation)
  }

}
