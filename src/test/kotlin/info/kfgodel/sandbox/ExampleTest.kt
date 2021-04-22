package info.kfgodel.sandbox

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Example test for creatin others
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class ExampleTest : KotlinSpec() {
  val log: Logger = LoggerFactory.getLogger(this::class.java);

  override fun define() {
    describe("a context") {
      val testVariable by let { 2 }

      it("can be used on a test") {
        log.info("Testing log fwk config")
        assertThat(testVariable()).isEqualTo(2)
      }
    }
  }
}
