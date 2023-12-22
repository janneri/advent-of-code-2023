// See puzzle in https://adventofcode.com/2023/day/21
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.sumN

class Day21(val inputLines: List<String>) {
    private val coords = inputLines.flatMapIndexed { y, line ->
        line.mapIndexed { x, _ -> Coord(x, y) }
    }.toSet()

    private val startCoord =
        coords.find { tileAt(it) == 'S' } ?: error("Start not found")

    private fun tileAt(coord: Coord) =
        inputLines[coord.y][coord.x]

    private fun inGrid(coord: Coord) =
        coord.x in inputLines[0].indices && coord.y in inputLines.indices

    private fun canStep(coord: Coord) =
        inGrid(coord) && tileAt(coord) in ".S"

    private fun getNext(coords: Set<Coord>): Set<Coord> =
        coords.flatMap { c -> c.neighbors().filter { canStep(it) } }.toSet()

    private fun countPositions(start: Coord, steps: Int): Long {
        var current = setOf(start)
        repeat(steps) {
            current = getNext(current)
        }
        return current.size.toLong()
    }

    fun part1(): Long = countPositions(startCoord, 64)

    fun part2(steps: Int): Long {
        var count = 0L

        // grid size 131 can reach 202300 grid centers with 26501365 steps
        val gridSize = inputLines.size
        val gridCount = steps / gridSize

        // Far middle grids (top, right, left, bottom)
        val stepsToFarCenter = steps - 65
        val stepsToFarStart = stepsToFarCenter - 65
        count += countPositions(Coord(65, 130), steps - stepsToFarStart) // north
        count += countPositions(Coord(0, 65), steps - stepsToFarStart) // right
        count += countPositions(Coord(65, 0), steps - stepsToFarStart) // bottom
        count += countPositions(Coord(130, 65), steps - stepsToFarStart) // left

        // Small triangles almost fully outside diamond shape
        val stepsToSmallCorner = stepsToFarCenter - 65 + 66
        val smallCount = gridCount
        count += countPositions(Coord(0, 130), steps - stepsToSmallCorner) * smallCount // top right
        count += countPositions(Coord(130, 130), steps - stepsToSmallCorner) * smallCount // top left
        count += countPositions(Coord(130, 0), steps - stepsToSmallCorner) * smallCount // bottom left
        count += countPositions(Coord(0, 0), steps - stepsToSmallCorner) * smallCount // bottom right

        // Polygons cut by small triangles, almost fully inside diamond
        val stepsToBigCorner = stepsToSmallCorner - 131
        val bigCount = smallCount - 1
        count += countPositions(Coord(0, 130), steps - stepsToBigCorner) * bigCount // top right
        count += countPositions(Coord(130, 130), steps - stepsToBigCorner) * bigCount // top left
        count += countPositions(Coord(130, 0), steps - stepsToBigCorner) * bigCount // bottom left
        count += countPositions(Coord(0, 0), steps - stepsToBigCorner) * bigCount // bottom right

        // Full grids (even and odd like chess board black and white)
        val maxWithEvenSteps = countPositions(startCoord, 200)
        val maxWithOddSteps = countPositions(startCoord, 201)
        val fullGridsInMiddle = 2L * (gridCount - 1) + 1
        val oddGridsInMiddle = fullGridsInMiddle / 2
        val evenGridsInMiddle = oddGridsInMiddle + 1
        // next rows have 1 less even and 1 less odd
        count += maxWithEvenSteps * evenGridsInMiddle + maxWithOddSteps * oddGridsInMiddle // middle row
        count += sumN(oddGridsInMiddle - 1) * maxWithOddSteps // odd full grids on top of middle
        count += sumN(oddGridsInMiddle - 1) * maxWithOddSteps // odd full grids below middle
        count += sumN(evenGridsInMiddle - 1) * maxWithEvenSteps // even full grids on top of middle
        count += sumN(evenGridsInMiddle - 1) * maxWithEvenSteps // even full grids below middle

        return count
    }

}