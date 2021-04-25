package info.kfgodel.sandbox.digraph.impl

import info.kfgodel.sandbox.digraph.api.DEdge
import info.kfgodel.sandbox.digraph.api.DNode

/**
 * This type represents an edge implementation for an in-memory graph
 * Date: 24/4/21 - 23:22
 */
class MemoryEdge<N>(override val source: N, override val type: N, override val target: N) : DEdge<N> {

    override fun toString(): String {
        return "$source -$type-> $target"
    }
}