package info.kfgodel.sandbox.digraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.digraph.api.DEdge
import info.kfgodel.sandbox.digraph.api.DGraph
import info.kfgodel.sandbox.digraph.api.DNode
import java.util.stream.Stream

/**
 * This class implements the default in-memory DiGraph
 * Date: 22/4/21 - 01:30
 */
class InMemoryGraph<N> : DGraph<N> {

    private val nodes = ArrayList<N>()
    private val edges = ArrayList<DEdge<N>>()

    override fun nodes(): Stream<out N> {
        return Nary.from(nodes)
    }

    override fun edges(): Stream<out DEdge<N>> {
        return Nary.from(edges)
    }

    override fun addNode(node: N): DGraph<N> {
        nodes.add(node)
        return this
    }

    override fun addEdge(sourceNode: N, typeNode: N, targetNode: N): DGraph<N> {
        addNode(sourceNode)
        addNode(typeNode)
        addNode(targetNode)
        val createdEdge = MemoryEdge(sourceNode, typeNode, targetNode)
        edges.add(createdEdge)
        return this
    }
}