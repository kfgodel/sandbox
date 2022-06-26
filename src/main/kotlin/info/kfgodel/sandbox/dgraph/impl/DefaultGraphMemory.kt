package info.kfgodel.sandbox.dgraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.dgraph.api.DGraph
import info.kfgodel.sandbox.dgraph.api.GraphMemory

/**
 * This type implements the grpah memory with an internal big graph
 * Date: 2/5/21 - 13:42
 */
class DefaultGraphMemory : GraphMemory {
    private val state: DGraph = DefaultGraph()

    override fun state(): DGraph {
        return state
    }

    override fun remember(aGraph: DGraph) {
        aGraph.nodes().forEach { externalNode -> state.getNodeFor(externalNode.id) }
        aGraph.edges().forEach { externalEdge -> state.getEdgeFrom(externalEdge.source.id, externalEdge.type.id, externalEdge.target.id) }
    }

    override fun recall(aPattern: DGraph): Nary<DGraph> {
        return Nary.empty()
    }
}