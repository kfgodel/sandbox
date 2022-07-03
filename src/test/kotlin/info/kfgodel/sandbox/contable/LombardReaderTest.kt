package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.on
import info.kfgodel.contable.operations.Operation
import info.kfgodel.contable.operations.OperationType
import info.kfgodel.contable.reader.LombardReader
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import java.io.File

/**
 * Test for reader of lombard report
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class LombardReaderTest : KotlinSpec() {
  override fun define() {
    describe("a lombard reader") {
      val reader by let { LombardReader() }

      it("has no operations when created") {
        assertThat(reader().operations()).isEmpty()
      }

      it("can read an operation from a single lombard report line"){
        reader().addReport("08/05/2019 SUSCRIPCION 1,04936100 952 998,99 u\$s")
        assertThat(reader().operations()).containsExactly(
          Operation(OperationType.BUY, 952.of(LOMBARD).at(998.99.of(USD)), on(8,5,2019))
        )
      }

      itThrows(IllegalArgumentException::class.java, "when the input doesn't have proper format", {
        reader().addReport("bad format string")
      }, { e ->
        assertThat(e).hasMessage("Report line has unexpected format. Expected[08/05/2019 SUSCRIPCION 1,04936100 952 998,99 u\$s] got:[bad format string]")
      })

      it("can read multiple lines for a lombard report"){
        reader().addReport("27/12/2019 RESCATE 1,06748600 915 976,75 u\$s\n" +
                "08/05/2019 SUSCRIPCION 1,04936100 952 998,99 u\$s")
        assertThat(reader().operations()).containsExactly(
          Operation(OperationType.SELL, 915.of(LOMBARD).at(976.75.of(USD)), on(27,12,2019)),
          Operation(OperationType.BUY, 952.of(LOMBARD).at(998.99.of(USD)), on(8,5,2019))
        )
      }

      it("can read operations from a report file"){
        val reportFile = File(this.javaClass.classLoader.getResource("lombard.txt").file)
        reader().addReportFile(reportFile)
        assertThat(reader().operations()).hasSize(20)
      }
    }
  }
}



