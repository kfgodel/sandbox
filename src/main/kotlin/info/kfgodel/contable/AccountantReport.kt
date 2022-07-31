package info.kfgodel.contable

import info.kfgodel.contable.operations.Operation


/**
 * This class represents the summary report generated to close a fiscal year of fund operations
 * Date: 27/7/22 - 22:39
 */
class AccountantReport(private val ledger: Ledger, val year: Int, private val valueUnit: String) {
    fun operations(): List<Operation> {
        return operationsMatching { operation -> operation.moment.year == year }
    }

    fun valuationAtStart(): PortfolioValuation {
        val valuation = PortfolioValuation(valueUnit)
        valuation.includeAll(operationsMatching(beforeThisYear()))
        return valuation
    }

    fun valuationAtEnd(): PortfolioValuation {
        val valuation = valuationAtStart()
        valuation.removeProfitAndLosses() // We don't care about previous P&G, only the ones on this year
        valuation.includeAll(operationsMatching(fromThisYear()))
        return valuation
    }

    private fun beforeThisYear() = { operation: Operation -> operation.moment.year < year }
    private fun fromThisYear() = { operation: Operation -> operation.moment.year == year }
    private fun operationsMatching(operationFilter: (Operation) -> Boolean) =
        ledger.operations()
            .filter(operationFilter)

}