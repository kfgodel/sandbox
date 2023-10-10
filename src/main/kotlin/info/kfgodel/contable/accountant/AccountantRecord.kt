package info.kfgodel.contable.accountant

import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.valued.ValueChange
import java.util.LinkedList

/**
 * This represents a single record on the accountan report where an operation is recorded and the effect of
 * value change on
 * Date: 16/8/23 - 20:06
 */
class AccountantRecord(val operation: Operation, val startingValuation: Magnitude) {
  val changes = LinkedList<ValueChange<PortfolioOperation>>()
  var endingValuation = startingValuation

  fun addAll(updatedValues: List<ValueChange<PortfolioOperation>>) {
    changes.addAll(updatedValues)
  }

  fun finalValuation(totalValue: Magnitude) {
    endingValuation = totalValue
  }

  fun valuationDifference(): Magnitude {
    return endingValuation.minus(startingValuation)
  }
}
