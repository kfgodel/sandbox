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
        val reportYear by let { 2019 }
        val report by let {fund().reportFor(reportYear())}

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

        describe("when created for 2nd operation year") {
          reportYear.set { 2020 }

          it("includes all the operations done in that year"){
            assertThat(report().operations()).hasSize(4)
          }

          it("has previous balances and profits at the beginning of first year") {
            assertThat(report().valuationAtStart().balances()).isEqualTo(listOf<ValuedAsset>(
              0.of(LOMBARD).at(0.of(USD))
            ))
            assertThat(report().valuationAtStart().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              915.of(LOMBARD).at(16.59.of(USD)),
              37.of(LOMBARD).at(0.36.of(USD))
            ))
          }

          it("has balance for all operated assets at end of year"){
            assertThat(report().valuationAtEnd().balances()).isEqualTo(listOf<ValuedAsset>(
              0.of(LOMBARD).at(0.of(USD))
            ))
          }

          it("has profit an loss for all operations done"){
            assertThat(report().valuationAtEnd().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              936.of(LOMBARD).at(10.91.of(USD)),
              3732.of(LOMBARD).at(28.84.of(USD)),
              4659.of(LOMBARD).at(29.24.of(USD))
            ))
          }
        }

        describe("when created for 3rd operation year") {
          reportYear.set { 2021 }

          it("includes all the operations done in that year"){
            assertThat(report().operations()).hasSize(9)
          }

          it("has previous balances and profits at the beginning of first year") {
            assertThat(report().valuationAtStart().balances()).isEqualTo(listOf<ValuedAsset>(
              0.of(LOMBARD).at(0.of(USD))
            ))
            assertThat(report().valuationAtStart().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              915.of(LOMBARD).at(16.59.of(USD)),
              37.of(LOMBARD).at(0.36.of(USD)),
              936.of(LOMBARD).at(10.91.of(USD)),
              3732.of(LOMBARD).at(28.84.of(USD)),
              4659.of(LOMBARD).at(29.24.of(USD))
            ))
          }

          it("has balance for all operated assets at end of year"){
            assertThat(report().valuationAtEnd().balances()).isEqualTo(listOf<ValuedAsset>(
              0.of(LOMBARD).at(0.of(USD))
            ))
          }

          it("has profit an loss for all operations done"){
            assertThat(report().valuationAtEnd().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              9738.of(LOMBARD).at(86.75.of(USD)),
              4607.of(LOMBARD).at(18.27.of(USD)),
              11920.of(LOMBARD).at(40.81.of(USD)),
              4603.of(LOMBARD).at(13.97.of(USD)),
              5929.of(LOMBARD).at(17.19.of(USD)),
              478.of(LOMBARD).at(0.88.of(USD)),
              4596.of(LOMBARD).at(5.59.of(USD)),
              14195.of(LOMBARD).at(10.07.of(USD))
            ))
          }
        }
      }
    }
  }
}



