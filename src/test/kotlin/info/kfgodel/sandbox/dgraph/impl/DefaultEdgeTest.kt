package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.dgraph.api.DGraph
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
            val edge by let { graph().createEdgeFrom("A", "B", "C") }

            describe("from a default graph and 3 nodes") {
                graph { DefaultGraph() }
                val source by let { graph().getNodeFor("A") }
                val type by let { graph().getNodeFor("B") }
                val target by let { graph().getNodeFor("C") }

                it("goes from a source node to a target node and has a type (defined by another node)") {
                    assertThat(edge().source).isSameAs(source())
                    assertThat(edge().type).isSameAs(type())
                    assertThat(edge().target).isSameAs(target())
                }

                it("has a string representation like a typed arrow") {
                    assertThat(edge().toString()).isEqualTo("A-[B]->C")
                }

                describe("equality"){
                    it("is based on its nodes and direction"){
                        assertThat(edge()).isEqualTo(graph().createEdgeFrom("A","B","C"))
                        assertThat(edge()).isNotEqualTo(graph().createEdgeFrom("C","B","A"))
                    }
                    it("only applies to instances of edges") {
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
                        val unrelatedNode = graph().getNodeFor("Z")
                        assertThat(edge().contains(unrelatedNode)).isFalse()
                    }
                }
            }

        }
    }
}
