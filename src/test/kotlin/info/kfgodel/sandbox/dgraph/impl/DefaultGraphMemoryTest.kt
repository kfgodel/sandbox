package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.dgraph.api.DGraph
import info.kfgodel.sandbox.dgraph.api.DNode
import info.kfgodel.sandbox.dgraph.api.Unknown
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import java.util.stream.Stream

/**
 * Example test for creating others
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class DefaultGraphMemoryTest : KotlinSpec() {
  override fun define() {
    describe("a graph memory") {
      val memory by let { DefaultGraphMemory() }

      it("starts with an empty state") {
        assertThat(memory().state().isEmpty()).isTrue()
      }

      describe("when remembering a graph") {
        val action = { aGraph:DGraph ->  memory().remember(aGraph)}

        it("changes its state to contain the graph") {
          val aGraph = DefaultGraph()
          aGraph.getEdgeFrom("A","is","B")

          action(aGraph)

          assertThat(memory().state()).isEqualTo(aGraph)
        }

        it("combines the edges and nodes from every remembered graph") {
          val firstGraph = DefaultGraph()
          firstGraph.getEdgeFrom("A","is","B")
          val secondGraph = DefaultGraph()
          secondGraph.getEdgeFrom("A","is","C")

          action(firstGraph)
          action(secondGraph)

          val expectedState = DefaultGraph()
          expectedState.getEdgeFrom("A","is","B")
          expectedState.getEdgeFrom("A","is","C")
          assertThat(memory().state()).isEqualTo(expectedState)
          assertThat(memory().state().nodes().map(DNode::id) as Stream<*>).containsExactly("A","is","B","C")
        }
      }

      describe("when recalling a graph") {
        it("returns empty if current state doesn't match the given pattern"){
          val aPattern = DefaultGraph()
          aPattern.getEdgeFrom("A","is", Unknown())

          val result = memory().recall(aPattern)

          assertThat(result as Stream<*>).isEmpty()
        }

        xit("returns each possible combination when current state matches the given pattern"){
          val aIsB = DefaultGraph()
          aIsB.getEdgeFrom("A","is","B")
          val aIsC = DefaultGraph()
          aIsC.getEdgeFrom("A","is","C")
          memory().remember(aIsB);
          memory().remember(aIsC);

          val aPattern = DefaultGraph()
          aPattern.getEdgeFrom("A","is", Unknown())
          val result = memory().recall(aPattern)

          assertThat(result as Stream<*>).contains(aIsB, aIsC)
        }

      }
    }
  }
}
