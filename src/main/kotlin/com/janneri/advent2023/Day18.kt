// See puzzle in https://adventofcode.com/2023/day/18
package com.janneri.advent2023

import com.janneri.advent2023.util.Direction
import com.janneri.advent2023.util.Direction.*
import com.janneri.advent2023.util.LongCoord

class Day18(val inputLines: List<String>) {

    private fun parseSteps(useColor: Boolean) = inputLines.map { DigStep.of(it, useColor) }

    private data class DigStep(val direction: Direction, val amount: Int) {
        companion object {
            private val pattern = """^([A-Z]) (\d+) \((.+)\)$""".toRegex()

            fun of(input: String, useColor: Boolean): DigStep {
                val (directionStr, amountStr, color) = pattern.find(input)!!.destructured
                val meters = color.drop(1).dropLast(1).toInt(16)
                val amount = if (useColor) meters else amountStr.toInt()

                val colorDir = when (color.takeLast(1)) {
                    "0" -> RIGHT
                    "1" -> DOWN
                    "2" -> LEFT
                    else -> UP
                }
                val direction = if (useColor) colorDir else Direction.ofLetter(directionStr[0])

                return DigStep(direction, amount)
            }
        }
    }

    // Calculate the area (coords must be ordered)
    private fun shoelace(coords: List<LongCoord>) =
        (coords).windowed(2, 1).sumOf { it[0].x * it[1].y - it[0].y * it[1].x } / 2

    // Pick's theorem: A = i + b/2 - 1 (i=interior points, b=border points)
    private fun calculateArea(corners: List<LongCoord>, borderLen: Long): Long {
        val interiorPoints = shoelace(corners)
        return interiorPoints + borderLen / 2 + 1
    }

    private fun solve(steps: List<DigStep>): Long {
        var borderLen = 0L
        val corners = steps.fold(listOf(LongCoord(0,0))) { acc, step ->
            val currentCoord = acc.last()
            val next = currentCoord.move(step.direction, step.amount.toLong())
            borderLen += currentCoord.distanceTo(next)
            acc + next
        }

        return calculateArea(corners, borderLen)
    }

    fun part1(): Long = solve(parseSteps(false))

    fun part2(): Long = solve(parseSteps(true))

}
