package info.kfgodel.sandbox.digraph.api

/**
 * This type represents a directed edge, connecting 2 nodes with a direction
 * Date: 22/4/21 - 01:33
 */
interface DiEdge<N, E> {

    /**
     * The object that this edge represents in the graph and can be used to reference this edge
     */
    val reference: N

    /**
     * The source node where this edge originates
     */
    val source: DiNode<N, E>

    /**
     * The target node where this edge ends
     */
    val target: DiNode<N, E>
}