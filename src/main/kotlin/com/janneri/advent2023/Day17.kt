// See puzzle in https://adventofcode.com/2023/day/17
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.Direction
import com.janneri.advent2023.util.Direction.DOWN
import com.janneri.advent2023.util.Direction.RIGHT
import com.janneri.advent2023.util.Path
import com.janneri.advent2023.util.drawGrid
import java.util.*


class Day17(val inputLines: List<String>) {
    private val grid = Grid(inputLines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char -> Coord(x, y) to char.digitToInt() }
    }.toMap())

    private data class Crucible(val coord: Coord, val dir: Direction, val dirCount: Int = 0)

    private data class PathWithCost(val path: Path,
                                    val cost: Int,
                                    val crucible: Crucible) : Comparable<PathWithCost> {
        override fun compareTo(other: PathWithCost): Int =
            this.cost.compareTo(other.cost)
    }

    private data class Grid(val tiles: Map<Coord, Int>) {
        private val coords = tiles.keys
        private val maxX = coords.maxOfOrNull { it.x }!!
        private val maxY = coords.maxOfOrNull { it.y }!!

        private fun tileAt(coord: Coord): Int = tiles[coord] ?: error("coord not found")

        private fun outOfGrid(coord: Coord) =
            coord.x !in 0..maxX || coord.y !in 0..maxY


        fun heatLoss(maxStepsToDir: Int = 3, minStepsToDir: Int = 1): Int {
            val path = findPath(Coord(0, 0), Coord(maxX, maxY), maxStepsToDir, minStepsToDir)!!
            drawGrid(coords) { when {
                it in path.path -> '#'
                else -> tileAt(it).digitToChar()
            }}
            return path.cost
        }

        private fun neighbors(path: PathWithCost, maxStepsToDir: Int, minStepsToDir: Int): List<PathWithCost> {
            val currentDir = path.crucible.dir

            val left = movePath(path, currentDir.turnLeft(), minStepsToDir, minStepsToDir)
            val right = movePath(path, currentDir.turnRight(), minStepsToDir, minStepsToDir)

            val result = mutableListOf(left, right)

            // Moving straight either min amount of steps or 1 if we already moved
            if (path.crucible.dirCount >= minStepsToDir) {
                result.add(movePath(path, currentDir, 1, path.crucible.dirCount + 1))
            }
            else {
                result.add(movePath(path, currentDir, minStepsToDir, path.crucible.dirCount + minStepsToDir))
            }

            return result
                    .filter { !outOfGrid(it.crucible.coord) }
                    .filter { it.crucible.dirCount <= maxStepsToDir }
        }

        private fun movePath(path: PathWithCost, dir: Direction, steps: Int, dirCount: Int): PathWithCost {
            var newPath = mutableListOf<Coord>()
            var cost = 0
            (1..steps).map {step ->
                val newPos = path.crucible.coord.move(dir, step)
                if (!outOfGrid(newPos)) cost += tileAt(newPos)
                newPath.add(newPos)
            }
            return PathWithCost(
                path.path + newPath, path.cost + cost, Crucible(newPath.last(), dir, dirCount)
            )
        }

        private fun moveCrucible(crucible: Crucible, dir: Direction, steps: Int): PathWithCost {
            var cost = 0
            var path = mutableListOf(crucible.coord)
            (1..steps).map {step ->
                val newPos = crucible.coord.move(dir, step)
                cost += tileAt(newPos)
                path.add(newPos)
            }
            return PathWithCost(
                path, cost, Crucible(path.last(), dir, crucible.dirCount + steps)
            )
        }

        fun findPath(startPos: Coord, endPos: Coord, maxStepsToDir: Int, minStepsToDir: Int): PathWithCost? {
            val seen = mutableSetOf<Crucible>()
            val rightPath = moveCrucible(Crucible(startPos, RIGHT, 0), RIGHT, minStepsToDir)
            val downPath = moveCrucible(Crucible(startPos, DOWN, 0), DOWN, minStepsToDir)
            val queue = PriorityQueue<PathWithCost>().apply {
                add(rightPath)
                add(downPath)
            }

            while (queue.isNotEmpty()) {
                val pathWithCost = queue.poll()
                val crucible = pathWithCost.crucible

                if (crucible.coord == endPos) {
                    return pathWithCost
                }

                if (crucible !in seen) {
                    seen += crucible

                    neighbors(pathWithCost, maxStepsToDir, minStepsToDir)
                        .filter { it.crucible !in seen }
                        .forEach { neighbour ->
                            queue.add(neighbour)
                        }
                }
            }
            return null
        }

    }

    fun part1(): Int = grid.heatLoss(3, 1)

    fun part2(): Int = grid.heatLoss(10, 4)
}
