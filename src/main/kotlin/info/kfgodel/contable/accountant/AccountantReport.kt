package info.kfgodel.contable.accountant

import info.kfgodel.contable.concepts.Ledger
import info.kfgodel.contable.concepts.Operation


/**
 * This class represents the summary report generated to close a fiscal year of fund operations
 * Date: 27/7/22 - 22:39
 */
class AccountantReport(private val ledger: Ledger, val year: Int, private val valueUnit: String) {

  private var records = emptyList<AccountantRecord>()

  fun operations(): List<Operation> {
        return ledger.operationsMatching(fromThisYear())
    }

    fun valuationAtStart(): PortfolioValuation {
        val valuation = PortfolioValuation(valueUnit)
        valuation.includeAll(ledger.operationsMatching(beforeThisYear()))
        return valuation
    }

    fun valuationAtEnd(): PortfolioValuation {
        val valuation = valuationAtStart()
        valuation.removeProfitAndLosses() // We don't care about previous P&G, only the ones on this year
        this.records = valuation.includeAll(operations())
        return valuation
    }

    private fun beforeThisYear() = { operation: Operation -> operation.moment.year < year }
    private fun fromThisYear() = { operation: Operation -> operation.moment.year == year }

  fun records(): List<AccountantRecord> {
    return this.records;
  }
}
