package info.kfgodel.sandbox.digraph.api

import java.util.stream.Stream

/**
 * This type represents a directed graph (which is composed of nodes and directed edges connecting them)
 * Date: 22/4/21 - 01:29
 */
interface DGraph<N> {
    /**
     * Returns a stream with all the nodes belonging to this graph
     */
    fun nodes(): Stream<out N>

    /**
     * Returns a stream with all the nodes belonging to this graph
     */
    fun edges(): Stream<out DEdge<N>>

    /**
     * Creates a new node on this graph that has the given object as its contents
     */
    fun addNode(node: N): DGraph<N>

    /**
     * Creates 2 new nodes from the source and target contents, and an edge that connects them
     */
    fun addEdge(sourceNode: N, typeNode: N, targetNode: N): DGraph<N>
}