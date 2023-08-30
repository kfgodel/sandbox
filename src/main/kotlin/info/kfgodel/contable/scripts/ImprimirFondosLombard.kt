package info.kfgodel.contable.scripts

import info.kfgodel.contable.LombardFund
import info.kfgodel.contable.printer.CsvPrinter

fun main() {
    for (year in 2019..2023) {
        val report = LombardFund().reportFor(year)
      CsvPrinter(report)
        .print()

    }
}
