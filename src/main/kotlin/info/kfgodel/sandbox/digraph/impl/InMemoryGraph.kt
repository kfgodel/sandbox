package info.kfgodel.sandbox.digraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.digraph.api.DiEdge
import info.kfgodel.sandbox.digraph.api.DiGraph
import info.kfgodel.sandbox.digraph.api.DiNode
import java.util.stream.Stream

/**
 * This class implements the default in-memory DiGraph
 * Date: 22/4/21 - 01:30
 */
class InMemoryGraph<N,E> : DiGraph<N,E> {

    private val nodes = ArrayList<DiNode<N, E>>()
    private val edges = ArrayList<DiEdge<N, E>>()

    override fun nodes(): Stream<out DiNode<N, E>> {
        return Nary.from(nodes)
    }

    override fun edges(): Stream<out DiEdge<N, E>> {
        return Nary.from(edges)
    }

    override fun createNodeFrom(nodeContent: N): DiNode<N, E> {
        val createdNode = MemoryNode<N,E>(nodeContent)
        nodes.add(createdNode)
        return createdNode
    }

    override fun createEdgeFrom(sourceContent: N, edgeReference: N, targetContent: N): DiEdge<N, E> {
        val source = createNodeFrom(sourceContent)
        val target = createNodeFrom(targetContent)
        val createdEdge = MemoryEdge<N,E>(source, edgeReference, target)
        edges.add(createdEdge)
        return createdEdge
    }
}