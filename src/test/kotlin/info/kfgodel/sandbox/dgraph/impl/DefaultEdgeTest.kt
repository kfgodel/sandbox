package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.dgraph.api.DGraph
import info.kfgodel.sandbox.dgraph.api.DNode
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * Date: 26/4/21 - 23:15
 */
@RunWith(JavaSpecRunner::class)
class DefaultEdgeTest : KotlinSpec() {
    override fun define() {
        describe("a default edge") {
            val graph by let<DGraph>()
            val source by let<DNode>()
            val type by let<DNode>()
            val target by let<DNode>()
            val edge by let { graph().createEdge(source(), type(), target()) }

            describe("from a default graph and 3 nodes") {
                graph { DefaultGraph() }
                source { graph().createNode().withId("source") }
                type { graph().createNode().withId("type") }
                target { graph().createNode().withId("target") }

                it("goes from a source node to a target node and has a type (defined by another node)") {
                    assertThat(edge().source).isSameAs(source())
                    assertThat(edge().type).isSameAs(type())
                    assertThat(edge().target).isSameAs(target())
                }

                it("uses its nodes inside an arrow to represent its string") {
                    assertThat(edge().toString()).isEqualTo("source -[type]-> target")
                }

                describe("equality"){
                    it("is based on its nodes and direction"){
                        assertThat(edge()).isEqualTo(graph().createEdge(source(),type(),target()))
                        assertThat(edge()).isNotEqualTo(graph().createEdge(target(),type(),source()))
                    }
                    it("only applies to other edges") {
                        // This is probably something that cannot be asserted, but I leave this to document the intention
                        assertThat(edge()).isNotEqualTo("source -[type]-> target")
                    }
                }

                describe("contains") {
                    it("a node if it is either its source, target or type"){
                        assertThat(edge().contains(source())).isTrue()
                        assertThat(edge().contains(type())).isTrue()
                        assertThat(edge().contains(target())).isTrue()
                    }
                    it("no node that is not its source, target or type") {
                        val unrelatedNode = graph().createNode()
                        assertThat(edge().contains(unrelatedNode)).isFalse()
                    }
                }
            }

        }
    }
}
