package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.sandbox.dgraph.api.DEdge
import info.kfgodel.sandbox.dgraph.api.DNode

/**
 * Default implementation for a DGraph edge
 * Date: 24/4/21 - 23:22
 */
class DefaultEdge(override val source: DNode, override val type: DNode, override val target: DNode) : DEdge {

    override fun toString(): String {
        return "$source-[$type]->$target"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DEdge) return false
        if (source != other.source) return false
        if (type != other.type) return false
        if (target != other.target) return false
        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + target.hashCode()
        return result
    }
}