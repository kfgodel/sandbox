package info.kfgodel.sandbox.digraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.digraph.api.DEdge
import info.kfgodel.sandbox.digraph.api.DGraph
import info.kfgodel.sandbox.digraph.api.DNode
import java.util.stream.Stream

/**
 * Default implementation of a DGraph
 * Date: 22/4/21 - 01:30
 */
class DefaultGraph : DGraph {

    private val nodes = ArrayList<DNode>()
    private val edges = ArrayList<DEdge>()

    override fun nodes(): Stream<out DNode> {
        return Nary.from(nodes)
    }

    override fun edges(): Stream<out DEdge> {
        return Nary.from(edges)
    }

    override fun createNode(): DNode {
        val created = DefaultNode()
        nodes.add(created)
        return created
    }

    override fun crateEdge(source: DNode, type: DNode, target: DNode): DEdge {
        val created = DefaultEdge(source, type, target)
        edges.add(created)
        return created
    }

}