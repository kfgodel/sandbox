package info.kfgodel.sandbox.digraph.impl

import info.kfgodel.sandbox.digraph.api.DiEdge
import info.kfgodel.sandbox.digraph.api.DiNode

/**
 * This type represents an edge implementation for an in-memory graph
 * Date: 24/4/21 - 23:22
 */
class MemoryEdge<N,E>( override val source: DiNode<N,E>, override val reference: N,  override val target: DiNode<N,E>) : DiEdge<N,E> {

    override fun toString(): String {
        return "$source -$reference-> $target"
    }
}