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
class DiGraphTest : KotlinSpec() {

  override fun define() {
    describe("a DiGraph") {
      val grapqh by let { InMemoryGraph<String, String>() }

      it("is created without nodes or edges") {
        assertThat(grapqh().nodes()).isEmpty()
        assertThat(grapqh().edges()).isEmpty()
      }

      describe("node creation") {
        it("can be done from any object using it as its reference") {
          val anyObject = "example"
          val createdNode = grapqh().createNodeFrom(anyObject)
          assertThat(createdNode).isNotNull
          assertThat(createdNode.reference).isSameAs(anyObject)
        }
        it("returns the created node as part of the graph") {
          val reference = "other example"
          val createdNode = grapqh().createNodeFrom(reference)
          assertThat(grapqh().nodes()).contains(createdNode)
        }
      }

      describe("edge creation") {
        it("can be done from any 3 objects, using 2 as node references, and 1 edge reference to connect them") {
          val sourceReference = "source ref"
          val edgeReference = "edge ref"
          val targetReference = "target ref"
          val createdEdge = grapqh().createEdgeFrom(sourceReference, edgeReference, targetReference)
          assertThat(createdEdge).isNotNull
          assertThat(createdEdge.reference).isSameAs(edgeReference)
          assertThat(createdEdge.source.reference).isSameAs(sourceReference)
          assertThat(createdEdge.target.reference).isSameAs(targetReference)
        }
        it("creates the nodes and edges implicitly when created from their references") {
          val sourceReference = "source"
          val edgerReference = "edge"
          val targetReference = "target"
          val createdEdge = grapqh().createEdgeFrom(sourceReference, edgerReference, targetReference)
          assertThat(grapqh().edges()).contains(createdEdge)
          assertThat(grapqh().nodes()).contains(createdEdge.source, createdEdge.target)
        }
      }
    }
  }
}
