// See puzzle in https://adventofcode.com/2023/day/24
package com.janneri.advent2023

import com.janneri.advent2023.util.LongCoord
import com.janneri.advent2023.util.almostEqual
import com.janneri.advent2023.util.parseLongs
import com.janneri.advent2023.util.permutations

class Day24(val inputLines: List<String>) {

    data class Hailstone(val pos: LongCoord, val velocity: LongCoord) {
        // Transfer to general form y=ax+c
        private val slope: Double = velocity.y.toDouble() / velocity.x.toDouble()
        // c=y-ax
        private val c = pos.y - slope * pos.x

        private fun isInFuture(x: Double) =
            (pos.x <= x && velocity.x > 0) || (x <= pos.x && velocity.x < 0)



        fun willCollide(other: Hailstone, testArea: LongRange): Boolean {
            // parallel planes never collide
            if (slope.almostEqual(other.slope)) return false

            val (x, y) = intersection(other)
            val (min, max) = testArea.first.toDouble() to testArea.last.toDouble()
            val inArea = x in min..max && y in min..max

            return inArea && isInFuture(x) && other.isInFuture(x)
        }

        private fun intersection(other: Hailstone): Pair<Double, Double> {
            // y1 = ax + c
            // y2 = bx + d
            // ax + c = bx + d
            // x = d - c / a - b
            val x = (other.c - c) / (this.slope - other.slope)
            val y = slope * x + c
            return x to y
        }

        companion object {
            fun parse(str: String): Hailstone =
                str.split("@").let { (coordStr, velocityStr) ->
                    val coordParts = parseLongs(coordStr)
                    val velocityParts = parseLongs(velocityStr)
                    return Hailstone(
                        LongCoord(coordParts[0], coordParts[1]),
                        LongCoord(velocityParts[0], velocityParts[1])
                    )
                }
        }
    }

    private val hailStones = inputLines.map { Hailstone.parse(it) }

    fun part1(testArea: LongRange): Int {
        return hailStones.permutations().count { (h1, h2) ->
            h1.willCollide(h2, testArea)
        }
    }
    
    fun part2(): Int {
        return 0
    }
}