package info.kfgodel.contable.scripts

import info.kfgodel.contable.LombardFund
import info.kfgodel.contable.printer.ScreenPrinter

fun main() {
    for (year in 2019..2023) {
        val report = LombardFund().reportFor(year)
      ScreenPrinter(report)
        .print()

    }
}
