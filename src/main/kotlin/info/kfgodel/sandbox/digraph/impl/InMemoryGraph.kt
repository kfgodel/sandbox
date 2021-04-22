package info.kfgodel.sandbox.digraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.digraph.api.DiEdge
import info.kfgodel.sandbox.digraph.api.DiGraph
import java.util.stream.Stream

/**
 * This class implements the default in-memory DiGraph
 * Date: 22/4/21 - 01:30
 */
class InMemoryGraph<N> : DiGraph<N> {
    override fun nodes(): Stream<out N> {
        return Nary.empty()
    }

    override fun edges(): Stream<out DiEdge<N>> {
        return Nary.empty()
    }
}