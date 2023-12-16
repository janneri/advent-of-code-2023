// See puzzle in https://adventofcode.com/2023/day/14
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.Direction
import com.janneri.advent2023.util.Direction.*
import java.util.*

class Day14(val inputLines: List<String>) {
    private val grid = Grid.of(inputLines)

    private data class Grid(val tiles: SortedMap<Coord, Char>) {
        private val coords = tiles.keys
        private val height = 1 + coords.maxOfOrNull { it.y }!!
        private fun tileAt(coord: Coord) = tiles[coord] ?: error("coord not found")
        private fun isFloor(coord: Coord) = tiles.contains(coord) && tileAt(coord) == '.'

        fun getState(): Int = tiles.values.joinToString().hashCode()

        fun cycle() = listOf(UP, LEFT, DOWN, RIGHT).forEach { tilt(it) }

        private fun rocksSorted(axis: (Coord) -> Int) = tiles.filterValues { it == 'O' }.keys.sortedBy { axis(it) }

        fun tilt(direction: Direction) {
            val axis: (Coord) -> Int = if (direction in listOf(UP, DOWN)) {c: Coord -> c.y} else {c: Coord -> c.x}
            val rocks = when (direction) {
                UP -> rocksSorted { axis(it) }
                DOWN -> rocksSorted { -axis(it) }
                LEFT -> rocksSorted { axis(it) }
                else -> rocksSorted { -axis(it) }
            }
            rocks.forEach { coord ->
                val newPos = coord.moveUntil(direction) { axis(it) < 0 || !isFloor(it) }
                tiles[coord] = '.'
                tiles[newPos] = 'O'
            }
        }

        fun rockWeightSum() = tiles.filterValues { it == 'O' }.keys.sumOf { height - it.y }

        companion object {
            fun of(lines: List<String>) = Grid(lines.flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> Coord(x, y) to char }
            }.toMap().toSortedMap())
        }
    }

    fun part1(): Int {
        grid.tilt(UP)
        return grid.rockWeightSum()
    }
    
    fun part2(): Int {
        val state = mutableMapOf(grid.getState() to 0)

        repeat(1000) { idx ->
            grid.cycle()
            val newState = grid.getState()
            if (state.contains(newState)) {
                val target = 1000000000L
                val loopStart = state[newState]!!
                val loopSize = idx - loopStart
                val remaining = (target - idx - 1) % loopSize
                repeat(remaining.toInt()) {
                    grid.cycle()
                }
                return grid.rockWeightSum()
            }
            state[newState] = idx
        }

        return 0
    }
}