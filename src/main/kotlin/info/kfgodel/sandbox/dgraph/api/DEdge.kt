package info.kfgodel.sandbox.dgraph.api

/**
 * This type represents a directed edge, connecting 2 nodes with a direction
 * Date: 22/4/21 - 01:33
 */
interface DEdge {
    /**
     * The object that this edge represents in the graph and can be used to reference this edge
     */
    val type: DNode

    /**
     * The source node where this edge originates
     */
    val source: DNode

    /**
     * The target node where this edge ends
     */
    val target: DNode

    /**
     * Indicates if the given nodes is part of this edge. It needs to be the source, target or type of this instance
     */
    fun contains(node: DNode): Boolean {
        return node == source || node == type || node == target
    }

}