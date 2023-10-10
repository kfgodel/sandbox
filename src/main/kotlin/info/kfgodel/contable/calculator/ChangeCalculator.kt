package info.kfgodel.contable.calculator

/**
 * Thius type represents a change calculator that given operations for some asset calculates the value changes
 * that a new operation generates
 *
 * Date: 10/9/23 - 16:21
 */
class ChangeCalculator<A: AssetSource<A>>(private val inputOperations: List<A>) {
  /**
   * Calculates the effect of combining a new operation with the existing when creating the calculator.
   * This may reduce assets, increase it, revert it, or no effect at all.
   * Any value change is recorded in the result
   */
  fun calculateFor(newOperation: A): AssetVariation<A> {
    validateSameAsset(inputOperations, newOperation) // Sanity check to prevent mixing asset types

    val result = AssetVariation(inputOperations, newOperation)
    var currentOperation = newOperation
    while (currentOperation.hasAsset() && result.hasRemainingAsset()){ // We still have assets to calculate on both ends
      val oldestOperation = result.removeOldest()
      val oldestAsset = oldestOperation.asset()
      val currentAsset = currentOperation.asset()
      if(currentAsset.hasSameSignumAs(oldestAsset)){
        // Both operations go in the same direction without consuming each other. We keep both
        val (consumed, zero) = currentOperation.splitBy(currentAsset.amount.abs())
        result.keepAsOldest(oldestOperation)
        result.keepAsNewest(consumed)
        result.addOperation(currentOperation)
        currentOperation = zero // nothing left to distribute
      }else{
        // One operation reduces the other total or partially. We have a potential value change as well
        val (consumedOldest, remainingOldest) = oldestOperation.splitBy(currentAsset.amount.abs())
        val (consumedCurrent, remainingCurrent) = currentOperation.splitBy(oldestAsset.amount.abs())
        result.recordChange(consumedOldest, consumedCurrent)
        if(consumedOldest.hasAsset()){
          // Some of the asset has been reduced
          result.reduceOperation(consumedOldest)
        }
        if(remainingOldest.hasAsset()){
          // The oldest operation still has remaining value to use in next loop
          result.keepAsOldest(remainingOldest)
        }
        currentOperation = remainingCurrent
      }
    }
    if(currentOperation.hasAsset()){
      // We still have an amount to update after consuming all pre-existing values. This changes balance signum
      result.keepAsOldest(currentOperation)
      result.addOperation(currentOperation)
    }
    return result
  }

  /**
   * Validates that the input operations have the same type of asset than the new operation before doing calculations
   */
  private fun validateSameAsset(inputOperations: List<A>, newOperation: A) {
    val differentAsset = inputOperations.find { input -> input.asset().unit != newOperation.asset().unit }
    if (differentAsset != null) {
      throw UnsupportedOperationException("We cannot mix assets new[${newOperation.asset().unit}] and old[${differentAsset.asset().unit}]. New: $newOperation, Old: $differentAsset")
    }
  }
}