package info.kfgodel.sandbox.contable

import info.kfgodel.contable.LOMBARD
import info.kfgodel.contable.Ledger
import info.kfgodel.contable.USD
import info.kfgodel.contable.of
import info.kfgodel.contable.on
import info.kfgodel.contable.operations.OperationType.BUY
import info.kfgodel.contable.operations.OperationType.SELL
import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * First tests for defining functionality I need for contable
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class LedgerTest : KotlinSpec() {
  override fun define() {
    describe("a ledger") {
      val ledger by let { Ledger() }

      it("has no operations when created") {
        assertThat(ledger().operations()).isEmpty()
      }

      it("registers operations over time") {
        ledger().register(BUY.done(on(8,5,2019), 952.of(LOMBARD).at(998.99.of(USD))))
        ledger().register(SELL.done(on(9,5,2019), 10.of(LOMBARD).at(100.of(USD))))
        assertThat(ledger().operations()).hasSize(2)
      }

      describe("when valuating assets at a point in time") {
        val valuation by let { ledger().valuation(on(31,12,2020), USD)}

        it("has no assets to value if it has no operations") {
          assertThat(valuation().balances()).isEmpty()
        }

        describe("after a buy") {
          beforeEach {
            ledger().register(BUY.done(on(1,1,2020), 100.of(LOMBARD).at(100.of(USD))))
          }
          it("values an asset using its buy price") {
            assertThat(valuation().balances()).hasSize(1)

            val valuedAsset = valuation().balances()[0]
            assertThat(valuedAsset.asset()).isEqualTo(100.of(LOMBARD))
            assertThat(valuedAsset.value()).isEqualTo(100.of(USD))
          }

          it("deducts a sell of same asset from the valuation") {
            ledger().register(SELL.done(on(2,1,2020), 50.of(LOMBARD).at(100.of(USD))))
            assertThat(valuation().balances()).hasSize(1)

            val valuedAsset = valuation().balances()[0]
            assertThat(valuedAsset.asset()).isEqualTo(50.of(LOMBARD))
            assertThat(valuedAsset.value()).isEqualTo(50.of(USD))
          }

          describe("for a second time") {
            beforeEach {
              ledger().register(BUY.done(on(2,1,2020), 50.of(LOMBARD).at(100.of(USD))))
            }

            it("combines multiple buys into a single valuation") {
              assertThat(valuation().balances()).hasSize(1)

              val valuedAsset = valuation().balances()[0]
              assertThat(valuedAsset.asset()).isEqualTo(150.of(LOMBARD))
              assertThat(valuedAsset.value()).isEqualTo(200.of(USD))
            }

            it("deducts a sell from the oldest buy") {
              ledger().register(SELL.done(on(3,1,2020), 50.of(LOMBARD).at(1000.of(USD))))
              assertThat(valuation().balances()).hasSize(1)

              val valuedAsset = valuation().balances()[0]
              assertThat(valuedAsset.asset()).isEqualTo(100.of(LOMBARD))
              assertThat(valuedAsset.value()).isEqualTo(150.of(USD))
            }
          }
        }

        describe("after a sell") {
          beforeEach {
            ledger().register(SELL.done(on(1,1,2020), 100.of(LOMBARD).at(100.of(USD))))
          }
          it("has a positive value for a negative asset as debt") {
            assertThat(valuation().balances()).hasSize(1)

            val valuedAsset = valuation().balances()[0]
            assertThat(valuedAsset.asset()).isEqualTo((-100).of(LOMBARD))
            assertThat(valuedAsset.value()).isEqualTo(100.of(USD))
          }

          it("reduces debt valuation when buying same asset") {
            ledger().register(BUY.done(on(2,1,2020), 50.of(LOMBARD).at(100.of(USD))))
            assertThat(valuation().balances()).hasSize(1)

            val valuedAsset = valuation().balances()[0]
            assertThat(valuedAsset.asset()).isEqualTo((-50).of(LOMBARD))
            assertThat(valuedAsset.value()).isEqualTo(50.of(USD))
          }

          describe("for a second time") {
            beforeEach {
              ledger().register(SELL.done(on(2,1,2020), 50.of(LOMBARD).at(100.of(USD))))
            }

            it("combines multiple sells into a single valuation") {
              assertThat(valuation().balances()).hasSize(1)

              val valuedAsset = valuation().balances()[0]
              assertThat(valuedAsset.asset()).isEqualTo((-150).of(LOMBARD))
              assertThat(valuedAsset.value()).isEqualTo(200.of(USD))
            }

            it("reduces debt from the oldest sell when buying") {
              ledger().register(BUY.done(on(3,1,2020), 50.of(LOMBARD).at(100.of(USD))))
              assertThat(valuation().balances()).hasSize(1)

              val valuedAsset = valuation().balances()[0]
              assertThat(valuedAsset.asset()).isEqualTo((-100).of(LOMBARD))
              assertThat(valuedAsset.value()).isEqualTo(150.of(USD))
            }
          }
        }
      }

      describe("when filtering operations by a condition") {
        val operationsOf2020 by let { ledger().operationsMatching{ op -> op.moment.year == 2020 } }

        it("returns no operation if nothing matches"){
          assertThat(operationsOf2020()).isEmpty()
        }

        it("returns only the operations that matches the condition") {
          ledger().register(BUY.done(on(1,1,2019), 1.of(LOMBARD).at(1.of(USD))))
          ledger().register(BUY.done(on(1,1,2020), 2.of(LOMBARD).at(2.of(USD))))
          assertThat(operationsOf2020()).containsExactly(BUY.done(on(1,1,2020), 2.of(LOMBARD).at(2.of(USD))))
        }
      }
    }
  }
}



