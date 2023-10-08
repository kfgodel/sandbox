package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.USD
import info.kfgodel.contable.concepts.Ledger
import info.kfgodel.contable.of
import info.kfgodel.contable.reader.PatagoniaReportReader
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
      val ledger by let { Ledger().fromFile("lombard.txt", PatagoniaReportReader()) }

      it("has a ledger with all the operations in the lombard.txt file") {
        assertThat(ledger().operations()).hasSize(22)
      }

      it("can generate a yearly accountant report") {
        assertThat(ledger().reportFor(2019, USD)).isNotNull
      }

      describe("yearly report") {
        val reportYear by let { 2019 }
        val report by let {ledger().reportFor(reportYear(), USD)}

        it("is created for a year") {
          assertThat(report().year).isEqualTo(2019)
        }

        it("includes all the operations done in that year"){
          assertThat(report().operations()).hasSize(3)
        }

        it("has no balances or profits at the beginning of first year") {
          assertThat(report().valuationAtStart().balances()).isEmpty()
          assertThat(report().valuationAtStart().totalProfitOrLoss()).isEqualTo(0.of(USD))
        }

        it("has balance for all operated assets at end of year including profit balances"){
          assertThat(report().valuationAtEnd().balances()).isEqualTo(listOf(
            16.95.of(USD).at((-16.00281).of(LOMBARD)) // We won 16 USD equivalent to over selling 16 Lombard
          ))
        }

        it("has profit an loss for all operations done"){
          assertThat(report().valuationAtEnd().totalProfitOrLoss()).isEqualTo(16.95.of(USD))
        }

        describe("when created for 2nd operation year") {
          reportYear.set { 2020 }

          it("includes all the operations done in that year"){
            assertThat(report().operations()).hasSize(4)
          }

          it("has previous balances and profits at the beginning of first year") {
            assertThat(report().valuationAtStart().balances()).isEqualTo(listOf(
              16.95.of(USD).at((-16.00281).of(LOMBARD))
            ))
          }

          it("has balance for all operated assets at end of year"){
            assertThat(report().valuationAtEnd().balances()).isEqualTo(listOf(
              85.94.of(USD).at((-79.61952).of(LOMBARD)) // Accumulated profits
            ))
          }

          it("has profit and loss for all operations done"){
            assertThat(report().valuationAtEnd().totalProfitOrLoss()).isEqualTo(68.99.of(USD))
          }
        }

        describe("when created for 3rd operation year") {
          reportYear.set { 2021 }

          it("includes all the operations done in that year"){
            assertThat(report().operations()).hasSize(9)
          }

          it("has previous balances and profits at the beginning of first year") {
            assertThat(report().valuationAtStart().balances()).isEqualTo(listOf(
              85.94.of(USD).at((-79.61952).of(LOMBARD))
            ))
          }

          it("has balance for all operated assets at end of year"){
            assertThat(report().valuationAtEnd().balances()).isEqualTo(listOf(
              279.48.of(USD).at((-256.62405).of(LOMBARD)) // 3 years of profit
            ))
          }

          it("has profit an loss for all operations done"){
            assertThat(report().valuationAtEnd().totalProfitOrLoss()).isEqualTo(193.54.of(USD))
          }
        }
      }
    }
  }
}



