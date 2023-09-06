package info.kfgodel.contable.scripts

import info.kfgodel.contable.ARS
import info.kfgodel.contable.concepts.Ledger
import info.kfgodel.contable.printer.ScreenPrinter
import info.kfgodel.contable.reader.CryptoRecordReader

/**
 * Date: 27/8/23 - 18:47
 */
fun main() {
  val ledger = Ledger().fromFile("crypto.txt", CryptoRecordReader())
  for (year in 2019..2023) {
    val report = ledger.reportFor(year, ARS)
    ScreenPrinter(report).print()
  }

}