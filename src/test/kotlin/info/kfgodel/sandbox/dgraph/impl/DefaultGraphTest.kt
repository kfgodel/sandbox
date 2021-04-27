package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import java.util.stream.Stream

/**
 * Test the basic DiGraph interface
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class DefaultGraphTest : KotlinSpec() {

  override fun define() {
    describe("a DGraph") {
      val graph by let { DefaultGraph() }

      it("is created without nodes or edges") {
        assertThat(graph().nodes() as Stream<*>).isEmpty()
        assertThat(graph().edges() as Stream<*>).isEmpty()
      }

      describe("nodes") {
        it("are created from the graph") {
          val createdNode = graph().createNode()
          assertThat(graph().nodes() as Stream<*>).contains(createdNode)
        }
      }

      describe("edges") {
        it("are created from 3 nodes. Source, edge type and target nodes") {
          val sourceNode = graph().createNode()
          val edgeType = graph().createNode()
          val targetNode = graph().createNode()

          val createdEdge = graph().createEdge(sourceNode, edgeType, targetNode)

          assertThat(graph().edges() as Stream<*>).contains(createdEdge)
        }
      }

      it("uses its edges for its string representation and adds any unconnected node") {
        val nodeA = graph().createNode().withId("A")
        val nodeB = graph().createNode().withId("B")
        val nodeC = graph().createNode().withId("C")
        val nodeD = graph().createNode().withId("D")
        graph().createNode().withId("E")

        graph().createEdge(nodeA, nodeB, nodeC)
        graph().createEdge(nodeA, nodeB, nodeD)

        assertThat(graph().toString()).isEqualTo("{A -[B]-> C,\n" +
                "A -[B]-> D, E}")
      }

      describe("equality") {
        beforeEach {
          val nodeA = graph().createNode().withId("A")
          val nodeIs = graph().createNode().withId("is")
          val nodeB = graph().createNode().withId("B")
          graph().createEdge(nodeA, nodeIs, nodeB)
        }

        it("is defined by its contents (nodes and edges)") {
          // TODO: Reduce the verbosity required to populate the graph
          val otherGraph = DefaultGraph()
          val nodeA = otherGraph.createNode().withId("A")
          val nodeIs = otherGraph.createNode().withId("is")
          val nodeB = otherGraph.createNode().withId("B")
          otherGraph.createEdge(nodeA, nodeIs, nodeB)

          assertThat(graph()).isEqualTo(otherGraph)
        }

        it("differs if nodes don't match") {
          val extraNodeGraph = DefaultGraph()
          val nodeA = extraNodeGraph.createNode().withId("A")
          val nodeIs = extraNodeGraph.createNode().withId("is")
          val nodeB = extraNodeGraph.createNode().withId("B")
          extraNodeGraph.createEdge(nodeA, nodeIs, nodeB)
          extraNodeGraph.createNode().withId("extra node")

          assertThat(graph()).isNotEqualTo(extraNodeGraph)
        }

        it("differs if edges don't match") {
          val invertedGraph = DefaultGraph()
          val nodeB = invertedGraph.createNode().withId("B")
          val nodeIs = invertedGraph.createNode().withId("is")
          val nodeA = invertedGraph.createNode().withId("A")
          invertedGraph.createEdge(nodeB, nodeIs, nodeA)

          assertThat(graph()).isNotEqualTo(invertedGraph)
        }
      }



    }
  }
}
