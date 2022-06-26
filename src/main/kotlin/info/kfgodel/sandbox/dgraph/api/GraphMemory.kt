package info.kfgodel.sandbox.dgraph.api

import ar.com.kfgodel.nary.api.Nary

/**
 * This type represents a memory that stores data from graphs into a bigger inter-connected graph.
 * This allows it to find relationships between the different stored graphs recreate missing data
 *
 * Date: 2/5/21 - 13:38
 */
interface GraphMemory {
    /**
     * Returns a graph with the state of this memory connecting all know objects and edges
     */
    fun state(): DGraph

    /**
     * Stores the given graph in this memory making relationships with previous state based on equality for
     * the nodes
     */
    fun remember(aGraph: DGraph)

    /**
     * Retrieves graphs from this memory state that have the same structure as the given pattern but completing
     * unknowns with data from this state. It returns 1 graph per each possible combination
     */
    fun recall(aPattern: DGraph): Nary<DGraph>
}