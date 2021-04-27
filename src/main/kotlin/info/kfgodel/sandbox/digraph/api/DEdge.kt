package info.kfgodel.sandbox.digraph.api

/**
 * This type represents a directed edge, connecting 2 nodes with a direction
 * Date: 22/4/21 - 01:33
 */
interface DEdge {

    /**
     * The object that this edge represents in the graph and can be used to reference this edge
     */
    val type: Any

    /**
     * The source node where this edge originates
     */
    val source: Any

    /**
     * The target node where this edge ends
     */
    val target: Any

}