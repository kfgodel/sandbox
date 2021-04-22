package info.kfgodel.sandbox.digraph.api

/**
 * This type represents a directed edge, connecting 2 nodes with a direction
 * Date: 22/4/21 - 01:33
 */
interface DiEdge<N> {

    /**
     * The source node where this edge originates
     */
    fun source(): N

    /**
     * The target node where this edge ends
     */
    fun target(): N
}