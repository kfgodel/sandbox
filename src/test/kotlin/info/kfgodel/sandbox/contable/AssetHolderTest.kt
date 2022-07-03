package info.kfgodel.sandbox.contable

import info.kfgodel.contable.AssetHolder
import info.kfgodel.contable.of
import info.kfgodel.contable.on
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * First tests for defining functionality I need for contable
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class AssetHolderTest : KotlinSpec() {
  override fun define() {
    describe("an asset holder") {
      val holder by let { AssetHolder() }

      it("has no assets when created") {
        assertThat(holder().assets()).isEmpty()
      }

      it("can acquire assets on a date for a price") {
        holder().acquired(952.of("Lombard").at(998.99.of("USD")), on(8,5,2019))
        assertThat(holder().assets()).containsExactly(952.of("Lombard"), (-998.99).of("USD"))
      }

      it("can sell assets on a date for a price") {
        holder().sold(915.of("Lombard").at(976.75.of("USD")), on(27,12,2019))
        assertThat(holder().assets()).containsExactly((-915).of("Lombard"), 976.75.of("USD"))
      }

    }
  }
}



