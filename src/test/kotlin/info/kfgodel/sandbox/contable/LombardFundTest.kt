package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.LombardFund
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.valued.ValuedAsset
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

        it("has no balances or profits at the beginning of first year") {
          assertThat(report().valuationAtStart().balances()).isEmpty()
          assertThat(report().valuationAtStart().profitAndLosses()).isEmpty()
        }

        it("has balance for all operated assets at end of year"){
          assertThat(report().valuationAtEnd().balances()).isEqualTo(listOf<ValuedAsset>(
            0.of(LOMBARD).at(0.of(USD))
          ))
        }

        it("has profit an loss for all operations done"){
          assertThat(report().valuationAtEnd().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
            915.of(LOMBARD).at(16.59.of(USD)),
            37.of(LOMBARD).at(0.36.of(USD))
          ))
        }
      }
    }
  }
}



