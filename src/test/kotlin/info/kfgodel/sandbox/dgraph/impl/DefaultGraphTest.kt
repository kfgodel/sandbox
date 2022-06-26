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
        assertThat(graph().isEmpty()).isTrue
      }

      describe("node") {
        it("is created to represent an object in the graph") {
          val createdNode = graph().getNodeFor("an object")
          assertThat(graph().nodes() as Stream<*>).contains(createdNode)
        }

        it("is reused when representing equal objects") {
          val firstNode = graph().getNodeFor(StringBuilder("a different object").toString())
          val secondNode = graph().getNodeFor(StringBuilder("a different object").toString())
          assertThat(firstNode).isSameAs(secondNode)
          assertThat(graph().nodes() as Stream<*>).hasSize(1)
        }
      }

      describe("edge") {
        it("is created to represent a directed link from one object to other, with an edge type defined by a third object") {
          val createdEdge = graph().getEdgeFrom("A", " to ", "B")
          assertThat(graph().edges() as Stream<*>).contains(createdEdge)
        }

        it("is reused when representing equal link") {
          val firstEdge = graph().getEdgeFrom("A", " to ", "B")
          val secondEdge = graph().getEdgeFrom("A", " to ", "B")
          assertThat(firstEdge).isSameAs(secondEdge)
          assertThat(graph().edges() as Stream<*>).hasSize(1)
        }

        it("may implicitly create nodes for each linked object") {
          val createdEdge = graph().getEdgeFrom("A", " to ", "B")
          assertThat(graph().nodes() as Stream<*>).contains(createdEdge.source, createdEdge.type, createdEdge.target)
        }
      }

      it("has a string representation using the edges and unconnected nodes") {
        graph().getEdgeFrom("A", "B", "C")
        graph().getEdgeFrom("A", "B", "D")
        graph().getNodeFor("E")

        assertThat(graph().toString()).isEqualTo("{A-[B]->C,\n" +
                "A-[B]->D, E}")
      }

      describe("equality") {
        beforeEach {
          graph().getEdgeFrom("A", "is", "B")
        }

        it("is defined by its contents (nodes and edges)") {
          val otherGraph = DefaultGraph()
          otherGraph.getEdgeFrom("A", "is", "B")

          assertThat(graph()).isEqualTo(otherGraph)
        }

        it("differs if nodes don't match") {
          val extraNodeGraph = DefaultGraph()
          extraNodeGraph.getEdgeFrom("A", "is", "B")
          extraNodeGraph.getNodeFor("extra node")

          assertThat(graph()).isNotEqualTo(extraNodeGraph)
        }

        it("differs if edges don't match") {
          val invertedGraph = DefaultGraph()
          invertedGraph.getEdgeFrom("B", "is", "A")

          assertThat(graph()).isNotEqualTo(invertedGraph)
        }
      }
    }
  }
}
