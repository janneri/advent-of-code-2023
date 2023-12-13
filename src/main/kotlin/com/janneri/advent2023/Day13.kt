// See puzzle in https://adventofcode.com/2023/day/13
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.Direction
import com.janneri.advent2023.util.Direction.*

class Day13(val inputLines: List<String>) {
    val grids = inputLines.joinToString("\n").split("\n\n").map { Grid.of(it.split("\n")) }

    data class Grid(val tiles: Map<Coord, Char>) {
        private val coords = tiles.keys
        private val maxX = coords.maxOfOrNull { it.x }!!
        private val maxY = coords.maxOfOrNull { it.y }!!

        private fun tileAt(coord: Coord) = tiles[coord] ?: error("coord not found")

        private fun countDiffs(coord1: Coord, coord2: Coord, direction: Direction): Int {
            var result = 0
            var nextCoord1 = coord1
            var nextCoord2 = coord2
            do {
                if (tileAt(nextCoord1) != tileAt(nextCoord2)) result += 1
                nextCoord1 = nextCoord1.move(direction)
                nextCoord2 = nextCoord2.move(direction)
            } while ((coords.contains(nextCoord1) && coords.contains(nextCoord2)))
            return result
        }

        private fun expand(coord1: Coord, coord2: Coord, direction: Pair<Direction, Direction>): Pair<Coord, Coord>? {
            val (nextCoord1, nextCoord2) = coord1.move(direction.first) to coord2.move(direction.second)
            return if (listOf(nextCoord1, nextCoord2).all { coords.contains(it) }) nextCoord1 to nextCoord2 else null
        }

        fun findPotentialReflectionLine(allowedDiff: Int, vertical: Boolean): Int? {
            val maxAxis = if (vertical) maxX else maxY
            val scanDir = if (vertical) DOWN else RIGHT
            val expandDir = if (vertical) LEFT to RIGHT else UP to DOWN
            val sFun: (Int) -> Coord = if (vertical) { x -> Coord(x, 0) } else { y -> Coord(0, y) }

            return (1..maxAxis).find { xOrY ->
                val startCoord = sFun(xOrY)
                var diffCount = countDiffs(startCoord.move(expandDir.first), startCoord, scanDir)
                if (diffCount <= allowedDiff) {
                    // the 2 side-by-side columns are similar, lets check other columns by expanding the search
                    var coordPair = expand(startCoord.move(expandDir.first), startCoord, expandDir)
                    while (coordPair != null && diffCount <= allowedDiff) {
                        diffCount += countDiffs(coordPair.first, coordPair.second, scanDir)
                        coordPair = expand(coordPair.first, coordPair.second, expandDir)
                    }

                    coordPair == null && diffCount == allowedDiff
                } else {
                    false
                }
            }
        }

        companion object {
            fun of(lines: List<String>) = Grid(lines.flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> Coord(x, y) to char }
            }.toMap())
        }
    }


    fun part1(): Int = grids.sumOf { grid ->
        (grid.findPotentialReflectionLine(0, true) ?: 0) +
        100 * (grid.findPotentialReflectionLine(0, false) ?: 0)
    }

    fun part2(): Int = grids.sumOf { grid ->
        (grid.findPotentialReflectionLine(1, true) ?: 0) +
        100 * (grid.findPotentialReflectionLine(1, false) ?: 0)
    }

}