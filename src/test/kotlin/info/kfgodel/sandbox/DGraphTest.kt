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

        it("does not create duplicates based on object equality"){
          val content = CharArray(1) { _ -> 'a' }
          val firstObject = String(content)
          val secondObject = String(content)
          grapqh().addNode(firstObject)

          val existingNode = grapqh().addNode(secondObject)

          assertThat(existingNode).isSameAs(firstObject)
          assertThat(grapqh().nodes()).hasSize(1)
        }
      }

      describe("edges") {
        it("are created from any 3 objects. Source, edge type and target nodes") {
          val sourceNode = "source"
          val edgeType = "type"
          val targetNode = "target"

          val createdEdge = grapqh().addEdge(sourceNode, edgeType, targetNode)

          assertThat(grapqh().edges()).contains(createdEdge)
          assertThat(createdEdge.source).isSameAs(sourceNode)
          assertThat(createdEdge.type).isSameAs(edgeType)
          assertThat(createdEdge.target).isSameAs(targetNode)
        }

        it("can implicitly create nodes when required") {
          val sourceNode = "source"
          val edgeType = "type"
          val targetNode = "target"
          grapqh().addEdge(sourceNode, edgeType, targetNode)

          assertThat(grapqh().nodes()).contains(sourceNode, edgeType, targetNode)
        }

        it("does not create duplicates based on its nodes equality") {
          val firstEdge = grapqh().addEdge("source", "type", "target")
          val secondEdge = grapqh().addEdge("source", "type", "target")
          assertThat(firstEdge).isSameAs(secondEdge)
          assertThat(grapqh().edges()).hasSize(1)
        }
      }
    }
  }
}
