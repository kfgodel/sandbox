package info.kfgodel.sandbox

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.digraph.impl.InMemoryGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * Test the basic DiGraph interface
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class DiGraphTest : KotlinSpec() {

  override fun define() {
    describe("a DiGraph") {
      val grapqh by let { InMemoryGraph<Any>() }

      it("is created without nodes or edges") {
        assertThat(grapqh().nodes()).isEmpty()
        assertThat(grapqh().edges()).isEmpty()
      }
    }
  }
}
