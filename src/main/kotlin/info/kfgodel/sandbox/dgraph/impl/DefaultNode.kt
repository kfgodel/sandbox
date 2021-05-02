package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.sandbox.dgraph.api.DNode

/**
 * Default implementation of a DGraph node
 * Date: 26/4/21 - 22:24
 */
class DefaultNode(override val id: Any) : DNode {
    override fun toString(): String {
        return id.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DNode) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}