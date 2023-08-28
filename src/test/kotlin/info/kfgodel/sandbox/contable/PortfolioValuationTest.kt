package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.PortfolioValuation
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.on
import info.kfgodel.contable.operations.OperationType.BUY
import info.kfgodel.contable.operations.OperationType.SELL
import info.kfgodel.contable.valued.ValuedAsset
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * Test to verify that portfolio valuation can keep track of losses and gains as well as balances
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class PortfolioValuationTest : KotlinSpec() {
  override fun define() {
    describe("a portfolio valuation") {
      val valuation by let { PortfolioValuation(USD) }

      it("starts with no balances") {
        assertThat(valuation().balances()).isEmpty()
      }

      it("starts with no profit or losses"){
        assertThat(valuation().profitAndLosses()).isEmpty()
      }

      itThrows(UnsupportedOperationException::class.java, "if a value with different unit is included",{
        valuation().include(BUY.done(on(1,1,2001),1.of("ANY").at(1.of("ARS"))))
      }, { e ->
        assertThat(e).hasMessage("Include using a different value[ARS] than expected[USD]")
      })

      describe("when values are included") {
        beforeEach {
          valuation().include(
            BUY.done(on(1,1,2001),100.of(LOMBARD).at(100.of(USD)))
          )
          valuation().include(
            BUY.done(on(1,1,2001),100.of(LOMBARD).at(200.of(USD)))
          )
          valuation().include(
            BUY.done(on(1,1,2001),10.of("OTHER").at(10.of(USD)))
          )
        }

        describe("balances") {

          it("are calculated per each asset") {
            assertThat(valuation().balances()).isEqualTo(listOf<ValuedAsset>(
              200.of(LOMBARD).at(300.of(USD)),
              10.of("OTHER").at(10.of(USD))
            ))
          }

          it("are updated as new values are included"){
            valuation().include(SELL.done(on(1,1,2001),150.of(LOMBARD).at(100.of(USD))))
            valuation().include(BUY.done(on(1,1,2001),10.of("OTHER").at(50.of(USD))))
            valuation().include(SELL.done(on(1,1,2001),10.of("OTHER").at(50.of(USD))))

            assertThat(valuation().balances()).isEqualTo(listOf<ValuedAsset>(
              50.of(LOMBARD).at(100.of(USD)),
              10.of("OTHER").at(50.of(USD))
            ))
          }
        }

        describe("P&L") {
          it("does not change until an asset is reduced"){
            assertThat(valuation().profitAndLosses()).isEmpty()
            assertThat(valuation().totalProfitOrLoss()).isEqualTo(0.of(USD))
            assertThat(valuation().totalProfitOrLoss()).isEqualTo(0.of(USD))
          }

          it("does not change if asset is reduced at original price"){
            valuation().include(SELL.done(on(1,1,2001),50.of(LOMBARD).at(50.of(USD))))
            valuation().include(SELL.done(on(1,1,2001),5.of("OTHER").at(5.of(USD))))
            assertThat(valuation().profitAndLosses()).isEmpty()
            assertThat(valuation().totalProfitOrLoss()).isEqualTo(0.of(USD))
          }

          it("generates losses when reducing asset for a worse price"){
            valuation().include(SELL.done(on(1,1,2001),150.of(LOMBARD).at(75.of(USD))))
            assertThat(valuation().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              100.of(LOMBARD).at((-50).of(USD)),
              50.of(LOMBARD).at((-75).of(USD))
            ))
            assertThat(valuation().totalProfitOrLoss()).isEqualTo((-125).of(USD))
          }

          it("generates profit when reducing asset for a better price"){
            valuation().include(SELL.done(on(1,1,2001),150.of(LOMBARD).at(300.of(USD))))
            assertThat(valuation().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              100.of(LOMBARD).at(100.of(USD))
            ))
            assertThat(valuation().totalProfitOrLoss()).isEqualTo(100.of(USD))
          }

          it("generates profit and losses when price is better and worse than previous values"){
            valuation().include(SELL.done(on(1,1,2001),150.of(LOMBARD).at(200.of(USD))))
            assertThat(valuation().profitAndLosses()).isEqualTo(listOf<ValuedAsset>(
              100.of(LOMBARD).at(33.33.of(USD)),
              50.of(LOMBARD).at((-33.33).of(USD))
            ))
            assertThat(valuation().totalProfitOrLoss()).isEqualTo(0.of(USD))
          }

          it("can remove all profit and loses without affecting balances") {
            valuation().include(SELL.done(on(1,1,2001),150.of(LOMBARD).at(200.of(USD))))
            valuation().removeProfitAndLosses()

            assertThat(valuation().profitAndLosses()).isEmpty()
            assertThat(valuation().totalProfitOrLoss()).isEqualTo(0.of(USD))
            assertThat(valuation().balances()).isEqualTo(listOf<ValuedAsset>(
              50.of(LOMBARD).at(100.of(USD)),
              10.of("OTHER").at(10.of(USD))
            ))
          }
        }
      }
    }
  }
}



