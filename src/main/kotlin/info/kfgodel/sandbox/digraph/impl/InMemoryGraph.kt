package info.kfgodel.sandbox.digraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.digraph.api.DEdge
import info.kfgodel.sandbox.digraph.api.DGraph
import java.util.stream.Stream

/**
 * This class implements the default in-memory DiGraph
 * Date: 22/4/21 - 01:30
 */
class InMemoryGraph<N> : DGraph<N> {

    private val nodes = LinkedHashMap<N,N>()
    private val edges = LinkedHashMap<DEdge<N>, DEdge<N>>()

    override fun nodes(): Stream<out N> {
        return Nary.from(nodes.keys)
    }

    override fun edges(): Stream<out DEdge<N>> {
        return Nary.from(edges.keys)
    }

    override fun addNode(node: N): N {
        val existingNode = nodes.putIfAbsent(node, node)
        return existingNode ?: node
    }

    override fun addEdge(sourceNode: N, typeNode: N, targetNode: N): DEdge<N> {
        val existingSource = addNode(sourceNode)
        val existingType = addNode(typeNode)
        val existingTarget = addNode(targetNode)
        val createdEdge = InMemoryEdge(existingSource, existingType, existingTarget)
        val existingEdge = edges.putIfAbsent(createdEdge, createdEdge)
        return existingEdge ?: createdEdge
    }
}