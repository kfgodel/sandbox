package info.kfgodel.sandbox.dgraph.api

import ar.com.kfgodel.nary.api.Nary

/**
 * This type represents a directed graph (which is composed of nodes and directed edges connecting them)
 * Date: 22/4/21 - 01:29
 */
interface DGraph {
    /**
     * Returns a stream with all the nodes belonging to this graph
     */
    fun nodes(): Nary<out DNode>

    /**
     * Returns a stream with all the nodes belonging to this graph
     */
    fun edges(): Nary<out DEdge>

    /**
     * Creates (if not exists) a new node on this graph that has the given object as its contents.
     * It returns the created Node or a previously created for an equal object
     */
    fun getNodeFor(anObject: Any): DNode

    /**
     * Creates (if not exists) an edge with the given type connecting source to target.
     * It return the created edge or a previously created equal (if already existed)
     */
    fun createEdgeFrom(source: Any, type: Any, target: Any): DEdge
}