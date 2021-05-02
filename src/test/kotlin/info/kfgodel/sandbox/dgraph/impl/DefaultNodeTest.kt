package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.dgraph.api.DGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * Date: 26/4/21 - 22:54
 */
@RunWith(JavaSpecRunner::class)
class DefaultNodeTest : KotlinSpec() {
    override fun define() {
        describe("a default node") {
            val graph by let<DGraph>()
            val node by let { graph().getNodeFor("A") }

            describe("from a default graph") {
                graph { DefaultGraph() }

                it("uses its id for its string representation") {
                    assertThat(node().toString()).isEqualTo("A")
                }

                describe("equality") {
                    it("is based on its id") {
                        assertThat(node()).isEqualTo(DefaultNode("A"))
                        assertThat(node()).isNotEqualTo(DefaultNode("B"))
                    }
                    it("only applies to instances of nodes") {
                        // This is probably something that cannot be asserted, but I leave this to document the intention
                        assertThat(node()).isNotEqualTo("A")
                        assertThat(node()).isNotEqualTo('A')
                    }
                }
            }
        }
    }
}
