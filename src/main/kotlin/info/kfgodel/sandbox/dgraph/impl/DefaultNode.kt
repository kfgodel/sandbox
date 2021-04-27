package info.kfgodel.sandbox.dgraph.impl

import info.kfgodel.sandbox.dgraph.api.DNode
import java.util.UUID

/**
 * Default implementation of a DGraph node
 * Date: 26/4/21 - 22:24
 */
class DefaultNode() : DNode {
    private var _id: Any = UUID.randomUUID().toString()

    override val id: Any
        get() = _id

    override fun withId(newId: Any): DNode {
        // The mutability introduced by this method doesn't look right. I'll review later
        this._id = newId
        return this
    }

    override fun toString(): String {
        return _id.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DNode) return false
        if (_id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return _id.hashCode()
    }

}