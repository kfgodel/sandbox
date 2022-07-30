package info.kfgodel.sandbox.contable

import info.kfgodel.contable.PortfolioValuation
import info.kfgodel.contable.USD
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



    }
  }
}



