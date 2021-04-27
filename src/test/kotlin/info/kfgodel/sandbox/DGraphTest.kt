package info.kfgodel.sandbox

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.digraph.impl.DefaultGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith

/**
 * Test the basic DiGraph interface
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class DGraphTest : KotlinSpec() {

  override fun define() {
    describe("a DGraph") {
      val graph by let { DefaultGraph() }

      it("is created without nodes or edges") {
        assertThat(graph().nodes()).isEmpty()
        assertThat(graph().edges()).isEmpty()
      }

      describe("nodes") {
        it("are created from the graph") {
          val createdNode = graph().createNode()
          assertThat(graph().nodes()).contains(createdNode)
        }
      }

      describe("edges") {
        it("are created from 3 nodes. Source, edge type and target nodes") {
          val sourceNode = graph().createNode()
          val edgeType = graph().createNode()
          val targetNode = graph().createNode()

          val createdEdge = graph().crateEdge(sourceNode, edgeType, targetNode)

          assertThat(graph().edges()).contains(createdEdge)
          assertThat(createdEdge.source).isSameAs(sourceNode)
          assertThat(createdEdge.type).isSameAs(edgeType)
          assertThat(createdEdge.target).isSameAs(targetNode)
        }
      }

    }
  }
}
