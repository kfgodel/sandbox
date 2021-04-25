package info.kfgodel.sandbox.digraph.api

import java.util.stream.Stream

/**
 * This type represents a node from a Digraph that can be used as a node centered API to manipulate the graph
 * Date: 24/4/21 - 22:38
 */
interface DiNode<N, E> {
    /**
     * Returns the object that this node represents in the graph, and can be used to reference this node
     */
    val reference: N
}