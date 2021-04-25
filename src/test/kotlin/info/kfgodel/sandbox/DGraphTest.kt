package info.kfgodel.sandbox

import info.kfgodel.jspek.api.JavaSpecRunner
import info.kfgodel.jspek.api.KotlinSpec
import info.kfgodel.sandbox.digraph.api.DGraph
import info.kfgodel.sandbox.digraph.impl.InMemoryGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import java.util.stream.Stream

/**
 * Test the basic DiGraph interface
 * Date: 22/04/21 - 00:32
 */
@RunWith(JavaSpecRunner::class)
class DGraphTest : KotlinSpec() {

  override fun define() {
    describe("a DiGraph") {
      val grapqh by let { InMemoryGraph<String>() }

      it("is created without nodes or edges") {
        assertThat(grapqh().nodes()).isEmpty()
        assertThat(grapqh().edges()).isEmpty()
      }

      describe("nodes") {
        it("are created from any object") {
          val anyObject = "example"
          grapqh().addNode(anyObject)
          assertThat(grapqh().nodes()).contains(anyObject)
        }
      }

      describe("edges") {
        it("are created from any 3 objects. 2 as source and target node, the third as edge type node") {
          val sourceNode = "source"
          val edgeType = "type"
          val targetNode = "target"
          grapqh().addEdge(sourceNode, edgeType, targetNode)
          assertThat(grapqh().edges()).hasSize(1)

          val createdEdge = grapqh().edges().findFirst().get()
          assertThat(createdEdge.source).isSameAs(sourceNode)
          assertThat(createdEdge.type).isSameAs(edgeType)
          assertThat(createdEdge.target).isSameAs(targetNode)
        }

        it("can create nodes when created") {
          val sourceNode = "source"
          val edgeType = "type"
          val targetNode = "target"
          grapqh().addEdge(sourceNode, edgeType, targetNode)

          assertThat(grapqh().nodes()).contains(sourceNode, edgeType, targetNode)
        }
      }
    }
  }
}
