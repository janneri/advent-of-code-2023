// See puzzle in https://adventofcode.com/2023/day/11
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.drawGrid
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class Day11(val inputLines: List<String>, val expandAmount: Int = 1) {
    private val coords = inputLines.flatMapIndexed { y, line ->
        line.mapIndexed { x, _ -> Coord(x, y) }
    }.toSet()

    private val galaxyCols: Set<Int> = (0 until inputLines.first().length)
        .filter { x -> inputLines.indices.any { y -> inputLines[y][x] == '#' } }.toSet()

    private val galaxyRows: Set<Int> = inputLines.indices.filter { y -> inputLines[y].any { it == '#' } }.toSet()

    private fun tileAt(coord: Coord): Char = inputLines[coord.y][coord.x]

    private fun expandRows(rows: List<String>): List<String> {
        val expandedRows = mutableListOf<String>()
        rows.forEach { line ->
            expandedRows.add(line)
            if (!line.contains('#')) expandedRows.add(line)
        }
        return expandedRows
    }

    private fun expandCols(lines: List<String>): List<String> {
        val width = lines.first().length
        val galaxyCols  = (0 until width)
            .filter { x -> lines.indices.any { y -> lines[y][x] == '#' } }.toSet()

        return lines.map { line ->
            line.foldIndexed("") { index, acc, c ->
                if (index in galaxyCols) acc + c else acc + c + c
            }
        }
    }


//    private data class PathWithCost(val path: Path, val cost: Int) : Comparable<PathWithCost> {
//        override fun compareTo(other: PathWithCost): Int =
//            this.cost.compareTo(other.cost)
//    }
//

//    private fun isValidPos(position: Coord): Boolean =
//        position.y in 0 until height && position.x in 0 until width
//
//    private fun findPath(startPos: Coord, endPos: Coord): Path? {
//        val seen = mutableSetOf<Coord>()
//        val queue = PriorityQueue<PathWithCost>().apply { add(PathWithCost(listOf(startPos), 0)) }
//
//        while (queue.isNotEmpty()) {
//            val (currentPath, currentCost) = queue.poll()
//            val currentCoord = currentPath.last()
//
//            if (currentCoord == endPos) {
//                return currentPath
//            }
//
//            if (currentCoord !in seen) {
//                seen += currentCoord
//
//                currentCoord.neighbors()
//                    .filter { it !in seen && isValidPos(it) }
//                    .forEach { neighbour ->
//                        queue.add(PathWithCost(currentPath + neighbour, currentCost + 1))
//                    }
//            }
//        }
//        return null
//    }

    private fun findPathLen(startPos: Coord, endPos: Coord): Int =
        abs(endPos.x - startPos.x) + abs(endPos.y - startPos.y)

    private fun findPathLen2(startPos: Coord, endPos: Coord): Int {
        val xCount = (min(startPos.x, endPos.x) .. max(startPos.x, endPos.x)).count { !galaxyCols.contains(it) }
        val yCount = (min(startPos.y, endPos.y) .. max(startPos.y, endPos.y)).count { !galaxyRows.contains(it) }
        val steps = abs(endPos.x - startPos.x) + abs(endPos.y - startPos.y) + xCount + yCount
        return steps
    }



    fun part1(): Int {
        drawGrid(coords) { c -> tileAt(c) }
        val galaxyCoords = coords.filter { tileAt(it) == '#' }

        val pairs = galaxyCoords.foldIndexed(mutableListOf<Pair<Coord, Coord>>()) { index, acc, coord ->
            if (index == galaxyCoords.size - 1) {
                acc
            } else {
                val range = index + 1 until galaxyCoords.size
                val newPairs = range.map { coord to galaxyCoords[it] }
                acc.addAll(newPairs)
                acc
            }
        }

        println("found: ${pairs.size}")
        val total = pairs.sumOf { findPathLen2(it.first, it.second) }
//        println(Coord(12, 7).neighbors())
//        println(Coord(12, 7).neighbors().filter { isValidPos(it) })

//        pairs.map { "from ${it.first} to ${it.second} : ${findPathLen(it.first, it.second)} vs ${findPathLen2(it.first, it.second)}" }.forEach { println(it) }
//        val total = galaxyCoords.foldIndexed(0) { index, acc, coord ->
//            if (index == galaxyCoords.size - 1) {
//                acc
//            } else {
//                val range = index until galaxyCoords.size
//                val pathLengths = range.map {findPath(coord, galaxyCoords[it])!!.size - 1 }
////                println(pathLengths)
//                acc + pathLengths.sum()
//            }
//        }
        return total
    }
    
    fun part2(): Int {
        return 0
    }
}