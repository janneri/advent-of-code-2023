// See puzzle in https://adventofcode.com/2023/day/23
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.Direction
import java.util.*

class Day23(val inputLines: List<String>) {
    val coords = inputLines.flatMapIndexed { y, line ->
        line.mapIndexed { x, _ -> Coord(x, y) }
    }.toSet()

    private fun tileAt(coord: Coord) =
        inputLines[coord.y][coord.x]

    private fun inGrid(coord: Coord) =
        coord.x in inputLines[0].indices && coord.y in inputLines.indices

    private fun canStep(coord: Coord) =
        inGrid(coord) && tileAt(coord) != '#'

    private fun neighbors(coord: Coord, useSlopes: Boolean): List<Coord> {
        if (!useSlopes) return coord.neighbors().filter { canStep(it) }

        val tile = tileAt(coord)
        if (tile in ">v<^") return listOf(coord.move(Direction.ofSymbol(tile)))

        val result = mutableListOf<Coord>()

        fun addCoordIfValid(direction: Direction, againstSlope: Char) =
            coord.move(direction).also {
                if (canStep(it) && tileAt(it) != againstSlope) result.add(it)
            }

        addCoordIfValid(Direction.UP, 'v')
        addCoordIfValid(Direction.RIGHT, '<')
        addCoordIfValid(Direction.DOWN, '^')
        addCoordIfValid(Direction.LEFT, '>')

        return result
    }

    private fun createGraph(useSlopes: Boolean = false): Map<Coord, Map<Coord, Int>> {
        val graph = mutableMapOf<Coord, MutableMap<Coord, Int>>()
        coords.filter { canStep(it) }.forEach { coord ->
            graph[coord] = neighbors(coord, useSlopes).associateWith { 1 }.toMutableMap()
        }

        // Graph densing is needed only in part 2
        if (useSlopes) return graph

        // Remove extra nodes from corridors
        do {
            val coord = graph.keys.find { graph.getValue(it).size == 2  }
            if (coord != null) {
                val neighbors = graph.getValue(coord)
                val start = neighbors.keys.first()
                val next = neighbors.keys.last()
                val newDist = graph.getValue(start).getValue(coord) + graph.getValue(coord).getValue(next)
                graph.getValue(start).remove(coord)
                graph.getValue(start)[next] = newDist
                graph.getValue(next).remove(coord)
                graph.getValue(next)[start] = newDist
                graph.remove(coord)
            }
        } while (coord != null)

        return graph
    }

    data class PathWithCost(val pos: Coord, val seen: Set<Coord>, val steps: Int) : Comparable<PathWithCost> {
        override fun compareTo(other: PathWithCost): Int =
            other.steps.compareTo(steps)
    }

    private fun findPath(startPos: Coord, endPos: Coord, graph: Map<Coord, Map<Coord, Int>>): Int {
        val queue = PriorityQueue<PathWithCost>().apply {
            add(PathWithCost(startPos, mutableSetOf(), 0))
        }

        val pathLenghts = mutableListOf<Int>()

        while (queue.isNotEmpty()) {
            val (coord, seenInThisPath, steps) = queue.poll()

            if (coord == endPos) {
                pathLenghts.add(steps)
            }

            if (coord != endPos && coord !in seenInThisPath) {
                graph.getValue(coord)
                    .filter { it.key !in seenInThisPath }
                    .forEach { neighbour ->
                        queue.add(PathWithCost(
                            neighbour.key,
                            seenInThisPath + coord,
                            steps + neighbour.value))
                    }
            }
        }

        return pathLenghts.max()
    }

    private fun solve(useSlopes: Boolean): Int {
        val start = Coord(1,0)
        val end = Coord(coords.maxOf { it.x } - 1, coords.maxOf { it.y })
        val graph = createGraph(useSlopes)
        return findPath(start, end, graph)
    }

    fun part1(): Int = solve(useSlopes = true)

    fun part2(): Int = solve(useSlopes = false)
}
