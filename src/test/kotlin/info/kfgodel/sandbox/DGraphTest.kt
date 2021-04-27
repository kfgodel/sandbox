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
class DGraphTest : KotlinSpec() {

  override fun define() {
    describe("a DGraph") {
      val graph by let { InMemoryGraph<String>() }

      it("is created without nodes or edges") {
        assertThat(graph().nodes()).isEmpty()
        assertThat(graph().edges()).isEmpty()
      }

      describe("nodes") {
        it("are created from any object") {
          val anyObject = "example"
          graph().addNode(anyObject)
          assertThat(graph().nodes()).contains(anyObject)
        }

        it("does not create duplicates based on object equality"){
          val content = CharArray(1) { _ -> 'a' }
          val firstObject = String(content)
          val secondObject = String(content)
          graph().addNode(firstObject)

          val existingNode = graph().addNode(secondObject)

          assertThat(existingNode).isSameAs(firstObject)
          assertThat(graph().nodes()).hasSize(1)
        }
      }

      describe("edges") {
        it("are created from any 3 objects. Source, edge type and target nodes") {
          val sourceNode = "source"
          val edgeType = "type"
          val targetNode = "target"

          val createdEdge = graph().addEdge(sourceNode, edgeType, targetNode)

          assertThat(graph().edges()).contains(createdEdge)
          assertThat(createdEdge.source).isSameAs(sourceNode)
          assertThat(createdEdge.type).isSameAs(edgeType)
          assertThat(createdEdge.target).isSameAs(targetNode)
        }

        it("can implicitly create nodes when required") {
          val sourceNode = "source"
          val edgeType = "type"
          val targetNode = "target"
          graph().addEdge(sourceNode, edgeType, targetNode)

          assertThat(graph().nodes()).contains(sourceNode, edgeType, targetNode)
        }

        it("does not create duplicates based on its nodes equality") {
          val firstEdge = graph().addEdge("source", "type", "target")
          val secondEdge = graph().addEdge("source", "type", "target")
          assertThat(firstEdge).isSameAs(secondEdge)
          assertThat(graph().edges()).hasSize(1)
        }
      }

      describe("equality") {
        it("is defined by its contents (nodes and edges)") {
          graph().addEdge("A","is","B")

          val otherGraph = InMemoryGraph<String>()
          otherGraph.addEdge("A","is","B")

          assertThat(graph()).isEqualTo(otherGraph)
          assertThat(graph()).isNotEqualTo(InMemoryGraph<String>())
        }

        it("differs if there is a difference in nodes") {
          graph().addEdge("A","is","B")

          val extraNodeGraph = InMemoryGraph<String>()
          extraNodeGraph.addEdge("A","is", "B")
          extraNodeGraph.addNode("D")

          assertThat(graph()).isNotEqualTo(extraNodeGraph)
        }

        it("differs if there is a difference in edges") {
          graph().addEdge("A","is","B")

          val differentEdgeGraph = InMemoryGraph<String>()
          differentEdgeGraph.addEdge("B","is", "A")

          assertThat(graph()).isNotEqualTo(differentEdgeGraph)
        }
      }
    }
  }
}
