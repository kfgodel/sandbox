package info.kfgodel.contable.calculator

import java.util.LinkedList

/**
 * This type represents the result of calculating the impact of one operation over others
 */
class AssetVariation<A: AssetSource<A>>(val startingOperations: List<A>, val modifyingOperation: A) {
  private val remainingOperations = LinkedList(startingOperations)
  private val changes = mutableListOf<Pair<A,A>>()
  private val addedOperations = mutableListOf<A>()
  private val reducedOperations = mutableListOf<A>()

  fun addedOperations(): List<A> {
    return addedOperations;
  }
  fun reducedOperations(): List<A> {
    return reducedOperations;
  }

  /**
   * State of original operations after the impact. It may result in the loss, gain or any combination of those
   */
  fun remainingOperations(): List<A> {
    return remainingOperations
  }

  /**
   * List of changes in the value of original operations after the impact of the new
   */
  fun changes(): List<Pair<A,A>> {
    return changes
  }

  fun hasRemainingAsset(): Boolean {
    return this.remainingOperations().isNotEmpty()
  }

  fun removeOldest(): A {
    return remainingOperations.removeFirst();
  }

  fun keepAsOldest(oldestOperation: A) {
    remainingOperations.addFirst(oldestOperation)
  }

  fun keepAsNewest(newest: A) {
    remainingOperations.addLast(newest)
  }

  fun recordChange(consumedOldest: A, consumedCurrent: A) {
    changes.add(Pair(consumedOldest, consumedCurrent))
  }

  fun addOperation(addedOperation: A) {
    addedOperations.add(addedOperation)
  }

  fun reduceOperation(reducedOperation: A) {
    reducedOperations.add(reducedOperation)
  }

}
