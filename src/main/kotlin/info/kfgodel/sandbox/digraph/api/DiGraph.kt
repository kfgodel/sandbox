package info.kfgodel.sandbox.digraph.api

import java.util.stream.Stream

/**
 * This type represents a directed graph (which is composed of nodes and directed edges connecting them)
 * Date: 22/4/21 - 01:29
 */
interface DiGraph<N> {
    /**
     * Returns a stream with all the nodes belonging to this graph
     */
    fun nodes(): Stream<out N>
    /**
     * Returns a stream with all the nodes belonging to this graph
     */
    fun edges(): Stream<out DiEdge<N>>
}