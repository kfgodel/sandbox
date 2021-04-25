package info.kfgodel.sandbox.digraph.api

import ar.com.kfgodel.nary.api.Nary

/**
 * This type represents a directed edge, connecting 2 nodes with a direction
 * Date: 22/4/21 - 01:33
 */
interface DEdge<N> {

    /**
     * The object that this edge represents in the graph and can be used to reference this edge
     */
    val type: N

    /**
     * The source node where this edge originates
     */
    val source: N

    /**
     * The target node where this edge ends
     */
    val target: N
}