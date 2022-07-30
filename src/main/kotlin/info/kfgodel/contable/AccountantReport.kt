package info.kfgodel.contable

import info.kfgodel.contable.operations.Operation


/**
 * This class represents the summary report generated to close a fiscal year of fund operations
 * Date: 27/7/22 - 22:39
 */
class AccountantReport(val year: Int,private val ledger: Ledger) {
    fun operations(): List<Operation> {
        return ledger.operations()
            .filter { operation -> operation.moment.year == year }
    }

    fun assets(): Set<Magnitude> {
        return ledger.assets(on(31,12,year))
    }

    fun profitAndLosses(): Set<Magnitude> {
        TODO("Not yet implemented")
    }
}