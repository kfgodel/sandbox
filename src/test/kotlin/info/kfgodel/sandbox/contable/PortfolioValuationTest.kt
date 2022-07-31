package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.PortfolioValuation
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
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

      itThrows(UnsupportedOperationException::class.java, "if a value with different unit is included",{
        valuation().include(1.of("ANY").at(1.of("ARS")))
      }, { e ->
        assertThat(e).hasMessage("Include using a different value[ARS] than expected[USD]")
      })

      describe("when values are included") {
        beforeEach {
          valuation().include(100.of(LOMBARD).at(100.of(USD)))
          valuation().include(100.of(LOMBARD).at(50.of(USD)))
          valuation().include(10.of("OTHER").at(10.of(USD)))
        }
        it("has a balance per each asset") {
          assertThat(valuation().balances()).isEqualTo(listOf(
            200.of(LOMBARD).at(150.of(USD)),
            10.of("OTHER").at(10.of(USD))
          ))
        }

        it("updates each balance as new values are included"){
          valuation().include((-150).of(LOMBARD).at(100.of(USD)))
          valuation().include(10.of("OTHER").at(50.of(USD)))
          valuation().include((-10).of("OTHER").at(50.of(USD)))

          assertThat(valuation().balances()).isEqualTo(listOf(
            50.of(LOMBARD).at(25.of(USD)),
            10.of("OTHER").at(50.of(USD))
          ))
        }
      }

    }
  }
}



