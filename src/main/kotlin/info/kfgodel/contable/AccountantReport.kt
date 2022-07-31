package info.kfgodel.contable

import info.kfgodel.contable.operations.Operation


/**
 * This class represents the summary report generated to close a fiscal year of fund operations
 * Date: 27/7/22 - 22:39
 */
class AccountantReport(private val ledger: Ledger, val year: Int, private val valueUnit: String) {
    fun operations(): List<Operation> {
        return ledger.operations()
            .filter { operation -> operation.moment.year == year }
    }

    fun valuationAtStart(): PortfolioValuation {
        return PortfolioValuation(valueUnit)
    }

    fun valuationAtEnd(): PortfolioValuation {
        val valuation = PortfolioValuation(valueUnit)
        operations().forEach(valuation::include)
        return valuation
    }
}