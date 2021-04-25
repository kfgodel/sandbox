package info.kfgodel.sandbox.digraph.impl

import info.kfgodel.sandbox.digraph.api.DEdge

/**
 * This type represents an edge implementation for an in-memory graph
 * Date: 24/4/21 - 23:22
 */
class InMemoryEdge<N>(override val source: N, override val type: N, override val target: N) : DEdge<N> {


    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + target.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(other !is DEdge<*>) return false
        if (source != other.source) return false
        if (type != other.type) return false
        if (target != other.target) return false
        return true
    }

    override fun toString(): String {
        return "$source -$type-> $target"
    }
}