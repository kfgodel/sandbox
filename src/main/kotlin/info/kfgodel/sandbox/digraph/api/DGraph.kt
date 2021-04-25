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
     * Creates (if not exists) a new node on this graph that has the given object as its contents.
     * It returns the created Node or a previously created equal
     */
    fun addNode(node: N): N

    /**
     * Creates (if not exists) an edge with the given type connecting source to target.
     * It return the created edge or a previously created equal (if already existed)
     */
    fun addEdge(sourceNode: N, typeNode: N, targetNode: N): DEdge<N>
}