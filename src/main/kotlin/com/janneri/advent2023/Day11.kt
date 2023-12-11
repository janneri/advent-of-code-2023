// See puzzle in https://adventofcode.com/2023/day/11
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class Day11(val inputLines: List<String>, private val expandAmount: Int = 1) {
    private val coords = inputLines.flatMapIndexed { y, line ->
        line.mapIndexed { x, _ -> Coord(x, y) }
    }.toSet()

    private val galaxyCoords = coords.filter { inputLines[it.y][it.x] == '#' }

    private val galaxyCols: Set<Int> = inputLines.first().indices
        .filter { x -> inputLines.indices.any { y -> inputLines[y][x] == '#' } }.toSet()

    private val galaxyRows: Set<Int> = inputLines.indices
        .filter { y -> inputLines[y].any { it == '#' } }.toSet()

    private fun findPathLen(startPos: Coord, endPos: Coord): Long {
        val xCount = (min(startPos.x, endPos.x) .. max(startPos.x, endPos.x)).count { !galaxyCols.contains(it) }
        val yCount = (min(startPos.y, endPos.y) .. max(startPos.y, endPos.y)).count { !galaxyRows.contains(it) }
        val steps = abs(endPos.x - startPos.x) + abs(endPos.y - startPos.y)
        return steps.toLong() + (expandAmount * xCount).toLong() + (expandAmount * yCount).toLong()
    }

    private fun solve(): Long {
        val pairs = galaxyCoords.foldIndexed(listOf<Pair<Coord, Coord>>()) { index, acc, coord ->
            if (index == galaxyCoords.lastIndex) {
                acc
            } else {
                val range = index + 1 until galaxyCoords.size
                acc + range.map { coord to galaxyCoords[it] }
            }
        }

        return pairs.sumOf { findPathLen(it.first, it.second) }
    }

    fun part1(): Long = solve()
    
    fun part2(): Long = solve()
}