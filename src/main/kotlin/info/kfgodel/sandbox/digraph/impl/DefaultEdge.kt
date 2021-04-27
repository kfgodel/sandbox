package info.kfgodel.sandbox.digraph.impl

import info.kfgodel.sandbox.digraph.api.DEdge
import info.kfgodel.sandbox.digraph.api.DNode

/**
 * Default implementation for a DGraph edge
 * Date: 24/4/21 - 23:22
 */
class DefaultEdge(override val source: DNode, override val type: DNode, override val target: DNode) : DEdge {

}