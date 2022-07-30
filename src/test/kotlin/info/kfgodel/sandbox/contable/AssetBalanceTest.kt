package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.valued.AssetBalance
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * This test verifies that balance operations are calculated properly using oldest asset first
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class AssetBalanceTest : KotlinSpec() {
  override fun define() {
    describe("an asset balance") {
      val balance by let { AssetBalance(LOMBARD, USD) }

      it("starts with 0 asset amount") {
        assertThat(balance().asset()).isEqualTo(0.of(LOMBARD))
      }
      it("starts with 0 value amount") {
        assertThat(balance().value()).isEqualTo(0.of(USD))
      }
      it("starts with no values"){
        assertThat(balance().values()).isEmpty()
      }
      it("it doesn't store zero asset values"){
        balance().updateWith(0.of(LOMBARD).at(1.of(USD)))
        assertThat(balance().values()).isEmpty()
      }

      itThrows(UnsupportedOperationException::class.java, "if updated with a different asset unit", {
        balance().updateWith(1.of("OTHER").at(1.of(USD)))
      },{ e ->
        assertThat(e).hasMessage("Update using a different asset[OTHER] than expected[LOMBARD]")
      })
      itThrows(UnsupportedOperationException::class.java, "if updated with a different value unit", {
        balance().updateWith(1.of(LOMBARD).at(1.of("OTHER")))
      },{ e ->
        assertThat(e).hasMessage("Update using a different value[OTHER] than expected[USD]")
      })

      describe("when balance is updated with a value") {
        beforeEach {
          balance().updateWith(100.of(LOMBARD).at(200.of(USD)))
        }

        it("stores the value with which is updated"){
          assertThat(balance().values()).isEqualTo(listOf(100.of(LOMBARD).at(200.of(USD))))
        }
        it("updates asset with given amount") {
          assertThat(balance().asset()).isEqualTo(100.of(LOMBARD))
        }
        it("updates value with  given amount") {
          assertThat(balance().value()).isEqualTo(200.of(USD))
        }

        describe("when updated with a second value that decreases balance") {
          describe("when second value is smaller than current balance") {
            beforeEach {
              balance().updateWith((-10).of(LOMBARD).at(100.of(USD)))
            }
            it("reduces asset by given amount") {
              assertThat(balance().asset()).isEqualTo(90.of(LOMBARD))
            }
            it("updates value proportional to remaining assets") {
              assertThat(balance().value()).isEqualTo(180.of(USD))
            }
            it("reduces stored value with given amount"){
              assertThat(balance().values()).isEqualTo(listOf(90.of(LOMBARD).at(180.of(USD))))
            }
          }
          describe("when second value is equal to current balance"){
            beforeEach {
              balance().updateWith((-100).of(LOMBARD).at(100.of(USD)))
            }
            it("reduces asset to 0") {
              assertThat(balance().asset()).isEqualTo(0.of(LOMBARD))
            }
            it("updates value to 0") {
              assertThat(balance().value()).isEqualTo(0.of(USD))
            }
            it("removes stored value"){
              assertThat(balance().values()).isEmpty()
            }
          }
          describe("when second value is bigger than current balance"){
            beforeEach {
              balance().updateWith((-200).of(LOMBARD).at(100.of(USD)))
            }
            it("inverts balance signum with remaining asset amount") {
              assertThat(balance().asset()).isEqualTo((-100).of(LOMBARD))
            }
            it("updates value proportional to remaining asset value") {
              assertThat(balance().value()).isEqualTo(50.of(USD))
            }
            it("replaces stored value with inverted signum remaining"){
              assertThat(balance().values()).isEqualTo(listOf((-100).of(LOMBARD).at(50.of(USD))))
            }
          }
        }

        describe("when updated with a second value that increases balance") {
          beforeEach {
            balance().updateWith(10.of(LOMBARD).at(50.of(USD)))
          }

          it("updates asset with adding given amount") {
            assertThat(balance().asset()).isEqualTo(110.of(LOMBARD))
          }
          it("updates value with adding given amount") {
            assertThat(balance().value()).isEqualTo(250.of(USD))
          }
          it("stores the additional value"){
            assertThat(balance().values()).isEqualTo(listOf(
              100.of(LOMBARD).at(200.of(USD)),
              10.of(LOMBARD).at(50.of(USD))
            ))
          }
        }

      }
    }
  }
}



