// See puzzle in https://adventofcode.com/2023/day/25
package com.janneri.advent2023

import com.janneri.advent2023.util.permutations
import java.util.*

private typealias Graph = MutableMap<String, MutableSet<String>>

class Day25(val inputLines: List<String>) {

    private fun parseConnections(line: String): Pair<String, Set<String>> {
        val (component, others) = line.split(":")
        val connectedComponents = others.trim().split(" ")
        return component to connectedComponents.toSet()
    }

    private fun parseGraph(): Graph =
        inputLines.fold(mutableMapOf()) { graph, line ->
            val (node, otherNodes) = parseConnections(line)
            otherNodes.forEach { graph.addEdge(node, it) }
            graph
        }

    private fun Graph.removeEdge(start: String, end: String) {
        this.getValue(start).remove(end)
        this.getValue(end).remove(start)
    }

    private fun Graph.addEdge(start: String, end: String) {
        getOrPut(start) { mutableSetOf() }.add(end)
        getOrPut(end) { mutableSetOf() }.add(start)
    }

    private fun Graph.countNodes(start: String): Int {
        val seen = mutableSetOf<String>()
        val queue = PriorityQueue<String>().apply { add(start) }
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            if (node !in seen) {
                seen += node
                this.getValue(node).forEach {
                    queue.add(it)
                }
            }
        }
        return seen.size
    }

    private fun Graph.getEdges(): Set<Pair<String, String>> {
        val result = mutableSetOf<Pair<String, String>>()
        this.forEach { (node, otherNodes) ->
            otherNodes.forEach { otherNode ->
                result += if (node < otherNode) node to otherNode
                else otherNode to node
            }
        }
        return result
    }

    data class NodeWithCost(val node: String, val steps: Int) : Comparable<NodeWithCost> {
        override fun compareTo(other: NodeWithCost): Int =
            steps.compareTo(other.steps)
    }

    private fun findStepsBetween(start: String, end: String, graph: Map<String, Set<String>>): Int {
        val seen = mutableSetOf<String>()
        val queue = PriorityQueue<NodeWithCost>().apply {
            add(NodeWithCost(start, 0))
        }

        while (queue.isNotEmpty()) {
            val (node, steps) = queue.poll()

            if (node == end) return steps

            if (node !in seen) {
                seen += node
                graph.getValue(node)
                    .forEach { neighbor ->
                        queue.add(NodeWithCost(neighbor, steps + 1))
                    }
            }
        }
        return -1
    }

    data class CuttableWire(val start: String, val end: String, val stepsWhenEdgeRemove: Int)

    private fun findCuttableWires(graph: Graph): List<CuttableWire> =
        graph.getEdges().map {(start, end) ->
            graph.removeEdge(start, end)
            val steps = findStepsBetween(start, end, graph)
            graph.addEdge(start, end)
            CuttableWire(start, end, steps)
        }

    fun part1(): Int {
        val graph = parseGraph()
        val totalNodeCount = graph.keys.size

        // We want to cut wires between nodes that are the farthest away of each others if the wire is cut.
        val cuttableWires = findCuttableWires(graph).sortedByDescending { it.stepsWhenEdgeRemove }

        // The actual input has 1 obvious wire to cut and then 3 uncertain wires.
        graph.removeEdge(cuttableWires.first().start, cuttableWires.first().end)

        // Find the correct wire-pair to cut with trial and error
        cuttableWires.drop(1).take(3).permutations().find { (wire1, wire2) ->
            graph.removeEdge(wire1.start, wire1.end)
            graph.removeEdge(wire2.start, wire2.end)
            val subgraphSize1 = graph.countNodes(wire1.start)
            val subgraphSize2 = graph.countNodes(wire1.end)
            if (subgraphSize1 < totalNodeCount && subgraphSize1 + subgraphSize2 == totalNodeCount) {
                return subgraphSize1 * subgraphSize2
            }
            else {
                graph.addEdge(wire1.start, wire1.end)
                graph.addEdge(wire2.start, wire2.end)
                false
            }
        }

        error("Could not find wires to cut")
    }

}