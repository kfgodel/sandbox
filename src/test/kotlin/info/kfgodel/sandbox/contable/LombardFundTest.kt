package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.LombardFund
import info.kfgodel.contable.of
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * Tests to verify yearly summaries are correctly calculated
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class LombardFundTest : KotlinSpec() {
  override fun define() {
    describe("a lombard fund") {
      val fund by let { LombardFund() }

      it("has a ledger with all the operations in the lombard.txt file") {
        assertThat(fund().ledger().operations()).hasSize(20)
      }

      it("can generate a yearly accountant report") {
        assertThat(fund().reportFor(2019)).isNotNull
      }

      describe("yearly report") {
        val report by let {fund().reportFor(2019)}

        it("is created for a year") {
          assertThat(report().year).isEqualTo(2019)
        }

        it("includes all the operations done in that year"){
          assertThat(report().operations()).hasSize(3)
        }

        it("can value the assets at the beginning of the year") {

        }

        it("indicates amount of each asset at the end of the year") {
          assertThat(report().assets()).containsExactly(0.of(LOMBARD), (16.95).of("USD"))
        }

        it("summarizes P&L due to operations in assets for that year") {
          assertThat(report().profitAndLosses()).containsExactly((16.95).of("USD"))
        }
      }

    }
  }
}



