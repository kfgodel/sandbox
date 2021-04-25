package info.kfgodel.sandbox.digraph.impl

import info.kfgodel.sandbox.digraph.api.DiNode

/**
 * This type implements an node for an in-memory graph
 * Date: 24/4/21 - 22:47
 */
class MemoryNode<N, E>(override val reference: N) : DiNode<N, E> {

    override fun toString(): String {
        return "$reference"
    }
}