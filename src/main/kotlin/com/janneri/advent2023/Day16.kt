// See puzzle in https://adventofcode.com/2023/day/16
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.Direction
import com.janneri.advent2023.util.Direction.*
import java.util.*

class Day16(val inputLines: List<String>) {
    private val grid = Grid.of(inputLines)

    data class Beam(var pos: Coord, var dir: Direction, var alive: Boolean = true)

    data class Grid(val tiles: SortedMap<Coord, Char>) {
        private val coords = tiles.keys
        val maxX = coords.maxOfOrNull { it.x }!!
        val maxY = coords.maxOfOrNull { it.y }!!
        private val reflections = mapOf(RIGHT to UP, DOWN to LEFT, UP to RIGHT, LEFT to DOWN)

        private fun tileAt(coord: Coord) = tiles[coord] ?: error("coord not found")
        private fun outOfGrid(coord: Coord) =
            coord.x !in 0..maxX || coord.y !in 0 .. maxY

        fun moveBeams(startBeam: Beam): Int {
            val beams = mutableListOf(startBeam)
            val seen = mutableSetOf<Beam>()

            while (beams.isNotEmpty()) {
                val newBeams = beams.flatMap { moveBeam(it, seen) }
                beams.removeIf { !it.alive }
                beams.addAll(newBeams)
            }
            return seen.map { it.pos }.toSet().count()
        }

        private fun moveBeam(beam: Beam, seen: MutableSet<Beam>): List<Beam> {
            val newBeams = mutableListOf<Beam>()
            val newPos = beam.pos.move(beam.dir)
            beam.pos = newPos

            if (outOfGrid(newPos)) {
                beam.alive = false
                return emptyList()
            }

            when (tileAt(newPos)) {
                '/' -> beam.dir = reflections[beam.dir] ?: error("xxx")
                '\\' -> beam.dir = reflections[beam.dir]?.turnOpposite() ?: error("xxx")
                '-' -> {
                    if (beam.dir in setOf(UP, DOWN)) {
                        newBeams.add(Beam(newPos, LEFT))
                        beam.dir = RIGHT
                    }
                }
                '|' -> {
                    if (beam.dir in setOf(LEFT, RIGHT)) {
                        newBeams.add(Beam(newPos, UP))
                        beam.dir = DOWN
                    }
                }
            }

            if (beam in seen) beam.alive = false
            else seen += beam.copy()

            return newBeams
        }

        companion object {
            fun of(lines: List<String>) = Grid(lines.flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> Coord(x, y) to char }
            }.toMap().toSortedMap())
        }
    }

    fun part1(): Int = grid.moveBeams(Beam(Coord(-1, 0), RIGHT))

    fun part2(): Int = listOf(
        (0..grid.maxY).map { Beam(Coord(-1, it), RIGHT) },
        (0..grid.maxY).map { Beam(Coord(grid.maxX + 1, it), LEFT) },
        (0..grid.maxX).map { Beam(Coord(it, -1), DOWN) },
        (0..grid.maxX).map { Beam(Coord(it, grid.maxY + 1), UP) })
        .flatten()
        .maxOfOrNull { grid.moveBeams(it) }!!
}