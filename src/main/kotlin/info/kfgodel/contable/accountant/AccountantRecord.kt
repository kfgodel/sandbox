package info.kfgodel.contable.accountant

import info.kfgodel.contable.concepts.Magnitude
import info.kfgodel.contable.concepts.Operation
import info.kfgodel.contable.valued.ValueChange

/**
 * This represents a single record on the accountan report where an operation is recorded and the effect of
 * value change on
 * Date: 16/8/23 - 20:06
 */
class AccountantRecord(val operation: Operation, val changes: List<ValueChange<Operation>>, val valuation: Magnitude) {
}
