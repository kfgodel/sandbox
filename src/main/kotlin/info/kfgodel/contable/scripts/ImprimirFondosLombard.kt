package info.kfgodel.contable.scripts

import info.kfgodel.contable.USD
import info.kfgodel.contable.concepts.Ledger
import info.kfgodel.contable.printer.CsvPrinter
import info.kfgodel.contable.reader.PatagoniaReportReader

fun main() {
  val ledger = Ledger().fromFile("lombard.txt", PatagoniaReportReader())
  for (year in 2019..2023) {
    val report = ledger.reportFor(year, USD)
    CsvPrinter(report).print()
  }
}
