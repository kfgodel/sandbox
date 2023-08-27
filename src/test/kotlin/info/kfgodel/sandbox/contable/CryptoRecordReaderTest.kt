package info.kfgodel.sandbox.contable

import info.kfgodel.contable.ARS
import info.kfgodel.contable.DAI
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.on
import info.kfgodel.contable.operations.OperationType.BUY
import info.kfgodel.contable.operations.OperationType.DEPOSIT
import info.kfgodel.contable.operations.OperationType.WITHDRAW
import info.kfgodel.contable.reader.CryptoRecordReader
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import java.io.File

/**
 * Test for reader of crypto reports containing transaction history
 * Date: 27/08/23 - 14:21
 */
@RunWith(JavaSpecRunner::class)
class CryptoRecordReaderTest : KotlinSpec() {
  override fun define() {
    describe("a crypto record reader") {
      val reader by let { CryptoRecordReader() }

      it("has no operations when created") {
        assertThat(reader().operations()).isEmpty()
      }

      it("can read an operation from a single report line"){
        reader().addReport("Buenbit\t2020-05-12\tCOMPRA\tDAI\t393.7\tARS\t-49999.9\t")
        assertThat(reader().operations()).containsExactly(
          BUY.done(on(12,5,2020), 393.7.of(DAI).at((-49999.9).of(ARS))).using("BuenBit")
        )
      }

      itThrows(IllegalArgumentException::class.java, "when the input doesn't have proper format", {
        reader().addReport("bad format string")
      }, { e ->
        assertThat(e).hasMessage("Report line has unexpected format. Expected[Buenbit\t2020-05-13\tTRANSFER\tDAI\t941.06\tDAI\t941.06\tSatoshi] got:[bad format string]")
      })

      it("can read multiple lines for a crypto report (returning in chronological order)"){
        reader().addReport("Buenbit\t2020-05-14\tDEPOSITO\t\t\tARS\t20000\tPatagonia 160\n" +
          "Buenbit\t2020-05-13\tRETIRO\t\t\tUSD\t-1006.93\tHSBC USD")
        assertThat(reader().operations()).containsExactly(
          WITHDRAW.done(on(13,5,2020), (-1006.93).of(USD).exchanged()).using("BuenBit").and("HSBC USD"),
          DEPOSIT.done(on(14,5,2020), 20000.of(ARS).exchanged()).using("BuenBit").and("Patagonia 160"),
        )
      }

      it("can read operations from a buenbit file"){
        val reportFile = File(this.javaClass.classLoader.getResource("buenbit.txt").file)
        reader().addReportFile(reportFile)
        assertThat(reader().operations()).hasSize(101)
      }

      it("can read operations from a satoshi file"){
        val reportFile = File(this.javaClass.classLoader.getResource("satoshi.txt").file)
        reader().addReportFile(reportFile)
        assertThat(reader().operations()).hasSize(82)
      }
    }
  }
}



