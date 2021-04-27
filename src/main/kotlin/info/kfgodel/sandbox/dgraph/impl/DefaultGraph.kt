package info.kfgodel.sandbox.dgraph.impl

import ar.com.kfgodel.nary.api.Nary
import info.kfgodel.sandbox.dgraph.api.DEdge
import info.kfgodel.sandbox.dgraph.api.DGraph
import info.kfgodel.sandbox.dgraph.api.DNode
import java.util.stream.Collectors

/**
 * Default implementation of a DGraph
 * Date: 22/4/21 - 01:30
 */
class DefaultGraph : DGraph {

    private val nodes = ArrayList<DNode>()
    private val edges = ArrayList<DEdge>()

    override fun nodes(): Nary<out DNode> {
        return Nary.from(nodes)
    }

    override fun edges(): Nary<out DEdge> {
        return Nary.from(edges)
    }

    override fun createNode(): DNode {
        val created = DefaultNode()
        nodes.add(created)
        return created
    }

    override fun createEdge(source: DNode, type: DNode, target: DNode): DEdge {
        val created = DefaultEdge(source, type, target)
        edges.add(created)
        return created
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("{")
        val edges = edges().limit(10)
            .map { obj -> obj.toString() }
            .collect(Collectors.joining(",\n"))
        if(edges.isNotEmpty()){
            builder.append(edges)
        }

        val remainingNodes = nodes().filter {node ->  edges().noneMatch {edge -> edge.contains(node)}}
            .limit(10)
            .map { obj -> obj.toString() }
            .collect(Collectors.joining(", "))
        if(remainingNodes.isNotEmpty()){
            if(edges.isNotEmpty()){
                builder.append(", ")
            }
            builder.append(remainingNodes)
        }
        builder.append("}")
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DGraph) return false
        if (nodes().toSet() != other.nodes().toSet()) return false
        if (edges().toSet() != other.edges().toSet()) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nodes.hashCode()
        result = 31 * result + edges.hashCode()
        return result
    }
}