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
            val node by let { graph().createNode() }

            describe("from a default graph") {
                graph { DefaultGraph() }

                it("has a UUID string as default id") {
                    assertThat(node().id as String).matches("^[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}$")
                }

                it("can change its id for any object") {
                    node().withId("A")
                    assertThat(node().id).isEqualTo("A")
                }

                it("uses its id for its string representation") {
                    node().withId(1234)
                    assertThat(node().toString()).isEqualTo("1234")
                }

                describe("equality") {
                    it("is based on its id") {
                        assertThat(node().withId(220)).isEqualTo(DefaultNode().withId(220))
                        assertThat(node().withId(220)).isNotEqualTo(DefaultNode().withId("220"))
                    }
                    it("only applies to other nodes") {
                        // This is probably something that cannot be asserted, but I leave this to document the intention
                        assertThat(node().withId(220)).isNotEqualTo(220)
                        assertThat(node().withId("220")).isNotEqualTo("220")
                    }
                }
            }
        }
    }
}
