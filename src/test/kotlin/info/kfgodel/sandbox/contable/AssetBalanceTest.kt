package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.on
import info.kfgodel.contable.operations.OperationType.BUY
import info.kfgodel.contable.operations.OperationType.SELL
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
      it("starts with no valuables"){
        assertThat(balance().valuables()).isEmpty()
      }
      it("it doesn't store zero as valuable"){
        balance().updateWith(BUY.done(on(1,1,2001),0.of(LOMBARD).at(1.of(USD))))
        assertThat(balance().valuables()).isEmpty()
      }

      itThrows(UnsupportedOperationException::class.java, "if updated with a different asset unit", {
        balance().updateWith(BUY.done(on(1,1,2001),1.of("OTHER").at(1.of(USD))))
      },{ e ->
        assertThat(e).hasMessage("Update using a different asset[OTHER] than expected[LOMBARD]")
      })
      itThrows(UnsupportedOperationException::class.java, "if updated with a different value unit", {
        balance().updateWith(
          BUY.done(on(1,1,2001),1.of(LOMBARD).at(1.of("OTHER")))
        )
      },{ e ->
        assertThat(e).hasMessage("Update using a different value[OTHER] than expected[USD]")
      })

      describe("when balance is updated with a value") {
        val firstUpdate by let { balance().updateWith(
          BUY.done(on(1,1,2001),100.of(LOMBARD).at(200.of(USD)))
        ) }
        beforeEach {
          firstUpdate()
        }

        it("stores the value with which is updated"){
          assertThat(balance().valuables()).isEqualTo(listOf(100.of(LOMBARD).at(200.of(USD))))
        }
        it("updates asset with given amount") {
          assertThat(balance().asset()).isEqualTo(100.of(LOMBARD))
        }
        it("updates value with given amount") {
          assertThat(balance().value()).isEqualTo(200.of(USD))
        }
        it("returns no consumed valuable"){
          assertThat(firstUpdate()).isEmpty()
        }

        describe("when updated with a 2nd value that decreases balance") {
          val decreasedAmount by let<Number>()
          val secondUpdate by let { balance().updateWith(
            SELL.done(on(1,1,2001), decreasedAmount().of(LOMBARD).at(100.of(USD)))
          ) }
          beforeEach {
            secondUpdate()
          }
          describe("when second value is smaller than current balance") {
            decreasedAmount.set { 10 }
            it("reduces asset by given amount") {
              assertThat(balance().asset()).isEqualTo(90.of(LOMBARD))
            }
            it("updates value proportional to remaining assets") {
              assertThat(balance().value()).isEqualTo(180.of(USD))
            }
            it("reduces stored valuables with given amount"){
              assertThat(balance().valuables()).isEqualTo(listOf(90.of(LOMBARD).at(180.of(USD))))
            }
            it("returns a change with partially consumed valuable and replaced valuable"){
              val change = secondUpdate()[0]
              assertThat(change.replaced()).isEqualTo(10.of(LOMBARD).at(20.of(USD)))
              assertThat(change.replacement()).isEqualTo(
                SELL.done(on(1,1,2001), 10.of(LOMBARD).at(100.of(USD)))
              )
            }
          }
          describe("when second value is equal to current balance"){
            decreasedAmount.set { 100 }
            it("reduces asset to 0") {
              assertThat(balance().asset()).isEqualTo(0.of(LOMBARD))
            }
            it("updates value to 0") {
              assertThat(balance().value()).isEqualTo(0.of(USD))
            }
            it("removes stored valuable"){
              assertThat(balance().valuables()).isEmpty()
            }
            it("returns change with consumed valuable and replacement value"){
              val change = secondUpdate()[0]
              assertThat(change.replaced()).isEqualTo(100.of(LOMBARD).at(200.of(USD)))
              assertThat(change.replacement()).isEqualTo(
                SELL.done(on(1,1,2001), 100.of(LOMBARD).at(100.of(USD)))
              )
            }
          }
          describe("when second value is bigger than current balance"){
            decreasedAmount.set { 200 }
            it("inverts balance signum with remaining asset amount") {
              assertThat(balance().asset()).isEqualTo((-100).of(LOMBARD))
            }
            it("updates value proportional to remaining asset value") {
              assertThat(balance().value()).isEqualTo(50.of(USD))
            }
            it("replaces valuables with remaining from difference"){
              assertThat(balance().valuables()).isEqualTo(listOf((-100).of(LOMBARD).at(50.of(USD))))
            }
            it("returns change with consumed valuable from previous balance and its partial replacement"){
              val change = secondUpdate()[0]
              assertThat(change.replaced()).isEqualTo(100.of(LOMBARD).at(200.of(USD)))
              assertThat(change.replacement()).isEqualTo(
                SELL.done(on(1,1,2001), 100.of(LOMBARD).at(50.of(USD)))
              )
            }
          }
        }

        describe("when updated with a 2nd value that increases balance") {
          val secondUpdate by let { balance().updateWith(
            BUY.done(on(1,1,2001),10.of(LOMBARD).at(50.of(USD)))
          ) }
          beforeEach {
            secondUpdate()
          }

          it("updates asset with adding given amount") {
            assertThat(balance().asset()).isEqualTo(110.of(LOMBARD))
          }
          it("updates value with adding given amount") {
            assertThat(balance().value()).isEqualTo(250.of(USD))
          }
          it("stores the additional valuable"){
            assertThat(balance().valuables()).isEqualTo(listOf(
              100.of(LOMBARD).at(200.of(USD)),
              10.of(LOMBARD).at(50.of(USD))
            ))
          }
          it("returns no consumed asset"){
            assertThat(secondUpdate()).isEmpty()
          }


          describe("but decreasing balance") {
            val decreasedAmount by let<Number>()
            val thirdUpdate by let { balance().updateWith(
              SELL.done(on(1,1,2001),decreasedAmount().of(LOMBARD).at(100.of(USD)))
            ) }
            beforeEach {
              thirdUpdate()
            }
            describe("when 3rd value is smaller than oldest value") {
              decreasedAmount.set { 10 }
              it("reduces asset by given amount from oldest value") {
                assertThat(balance().asset()).isEqualTo(100.of(LOMBARD))
              }
              it("reduces value proportionally from oldest asset") {
                assertThat(balance().value()).isEqualTo(230.of(USD))
              }
              it("reduces oldest stored valuable with given amount"){
                assertThat(balance().valuables()).isEqualTo(listOf(
                  90.of(LOMBARD).at(180.of(USD)),
                  10.of(LOMBARD).at(50.of(USD))
                ))
              }
              it("returns change with partially consumed oldest valuable and its replacement value"){
                val change = thirdUpdate()[0]
                assertThat(change.replaced()).isEqualTo(10.of(LOMBARD).at(20.of(USD)))
                assertThat(change.replacement()).isEqualTo(
                  SELL.done(on(1,1,2001), 10.of(LOMBARD).at(100.of(USD)))
                )
              }
            }
            describe("when 3rd value is equal to oldest value"){
              decreasedAmount.set { 100 }
              it("keeps assets from 2nd value") {
                assertThat(balance().asset()).isEqualTo(10.of(LOMBARD))
              }
              it("keeps value from 2nd value") {
                assertThat(balance().value()).isEqualTo(50.of(USD))
              }
              it("removes oldest stored valuable"){
                assertThat(balance().valuables()).isEqualTo(listOf(
                  10.of(LOMBARD).at(50.of(USD))
                ))
              }
              it("returns change with all consumed oldest valuable and its replacement value"){
                val change = thirdUpdate()[0]
                assertThat(change.replaced()).isEqualTo(100.of(LOMBARD).at(200.of(USD)))
                assertThat(change.replacement()).isEqualTo(
                  SELL.done(on(1,1,2001), 100.of(LOMBARD).at(100.of(USD)))
                )
              }
            }
            describe("when 3rd value is bigger than oldest value but smaller than 2nd"){
              decreasedAmount.set { 105 }
              it("reduces asset by given amount also from 2nd value") {
                assertThat(balance().asset()).isEqualTo(5.of(LOMBARD))
              }
              it("reduces value proportionally from 2nd asset") {
                assertThat(balance().value()).isEqualTo(25.of(USD))
              }
              it("removes 1st valuable and only keeps a portion of 2nd"){
                assertThat(balance().valuables()).isEqualTo(listOf(
                  5.of(LOMBARD).at(25.of(USD))
                ))
              }
              it("returns changes for all oldest valuable and a portion of consumed 2nd valuable and their replacement values"){
                val changes = thirdUpdate()
                assertThat(changes[0].replaced()).isEqualTo(100.of(LOMBARD).at(200.of(USD)))
                assertThat(changes[0].replacement()).isEqualTo(
                  SELL.done(on(1,1,2001), 100.of(LOMBARD).at(95.24.of(USD)))
                )

                assertThat(changes[1].replaced()).isEqualTo(5.of(LOMBARD).at(25.of(USD)))
                assertThat(changes[1].replacement()).isEqualTo(
                  SELL.done(on(1,1,2001), 5.of(LOMBARD).at(4.76.of(USD)))
                )
              }
            }
            describe("when 3rd value is bigger than previous values"){
              decreasedAmount.set { 200 }
              it("inverts balance signum with remaining asset amount") {
                assertThat(balance().asset()).isEqualTo((-90).of(LOMBARD))
              }
              it("updates value proportional to remaining asset value") {
                assertThat(balance().value()).isEqualTo(45.of(USD))
              }
              it("replaces valuables with remaining from difference"){
                assertThat(balance().valuables()).isEqualTo(listOf((-90).of(LOMBARD).at(45.of(USD))))
              }
              it("returns changes with all consumed valuables and their replacement values"){
                val changes = thirdUpdate()
                assertThat(changes[0].replaced()).isEqualTo(100.of(LOMBARD).at(200.of(USD)))
                assertThat(changes[0].replacement()).isEqualTo(
                  SELL.done(on(1,1,2001), 100.of(LOMBARD).at(50.of(USD)))
                )

                assertThat(changes[1].replaced()).isEqualTo(10.of(LOMBARD).at(50.of(USD)))
                assertThat(changes[1].replacement()).isEqualTo(
                  SELL.done(on(1,1,2001), 10.of(LOMBARD).at(5.of(USD)))
                )
              }
            }
          }
        }
      }
    }
  }
}



