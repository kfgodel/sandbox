package info.kfgodel.sandbox.dgraph.api

/**
 * This type represents a node from a Dgraph that can be used as a node centered API to manipulate the graph
 * Date: 24/4/21 - 22:38
 */
interface DNode {

    /**
     * An object that identifies this node.
     * Ids need to have a consistent equality criteria among nodes
     */
    val id: Any

}