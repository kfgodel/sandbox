package info.kfgodel.contable

import info.kfgodel.contable.reader.PatagoniaReportReader
import java.io.File

/**
 * This class represents the fund that contains all the operations done in Lombard funds
 * Date: 27/7/22 - 22:15
 */
class LombardFund {
    private val ledger: Ledger = readFromFile()

    private fun readFromFile(): Ledger {
        val ledger = Ledger()
        val reportFile = File(this.javaClass.classLoader.getResource("lombard.txt").file)
        PatagoniaReportReader().addReportFile(reportFile).operations().forEach(ledger::register)
        return ledger
    }

    fun ledger(): Ledger {
        return ledger
    }

    fun reportFor(year: Int): AccountantReport {
        return AccountantReport(ledger, year, USD)
    }
}