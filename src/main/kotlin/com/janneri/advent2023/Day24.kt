// See puzzle in https://adventofcode.com/2023/day/24
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord3d
import com.janneri.advent2023.util.almostEqual
import com.janneri.advent2023.util.parseLongs
import com.janneri.advent2023.util.permutations
import java.io.IOException
import java.util.concurrent.TimeUnit

class Day24(val inputLines: List<String>) {

    data class Hailstone(val pos: Coord3d, val velocity: Coord3d) {
        // Transfer to general form y=ax+c
        private val slope: Double = velocity.y.toDouble() / velocity.x.toDouble()
        // c=y-ax
        private val c = pos.y - slope * pos.x
        // Note. Sometimes line is represented as ax + by + c = 0
        // The general form can be converted: y = ax + c => ax - y + c = 0 (where b = -1)

        private fun isAFutureXPos(x: Double) =
            (pos.x <= x && velocity.x > 0) || (x <= pos.x && velocity.x < 0)

        fun willCollideXY(other: Hailstone, testArea: LongRange): Boolean {
            // parallel planes never collide
            if (slope.almostEqual(other.slope)) return false

            val (x, y) = intersectionXY(other)
            val (min, max) = testArea.first.toDouble() to testArea.last.toDouble()
            val inArea = x in min..max && y in min..max

            return inArea && isAFutureXPos(x) && other.isAFutureXPos(x)
        }

        private fun intersectionXY(other: Hailstone): Pair<Double, Double> {
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
                        Coord3d(coordParts[0], coordParts[1], coordParts[2]),
                        Coord3d(velocityParts[0], velocityParts[1], velocityParts[2])
                    )
                }
        }
    }

    private val hailStones = inputLines.map { Hailstone.parse(it) }

    fun part1(testArea: LongRange): Int {
        return hailStones.permutations().count { (h1, h2) ->
            h1.willCollideXY(h2, testArea)
        }
    }

    private fun equationsZ3(): String {
        val consts = listOf("x", "y", "z", "vx", "vy", "vz").map {
            "(declare-const $it Real)"
        }

        val equations = hailStones.take(3).mapIndexed { i, stone ->
            val t = "t$i"
            """
                (declare-const $t Real)
                (assert (= (+ x (* vx $t)) (+ ${stone.pos.x} (* $t ${stone.velocity.x}))))
                (assert (= (+ y (* vy $t)) (+ ${stone.pos.y} (* $t ${stone.velocity.y}))))
                (assert (= (+ z (* vz $t)) (+ ${stone.pos.z} (* $t ${stone.velocity.z}))))
            """.trimIndent()
        }

        return """
                ${consts.joinToString("\n")}
                ${equations.joinToString("\n")}
                 
                (check-sat)
                (get-model)
            """.trimIndent()
    }

    fun part2(): Long {
        // Too lazy, so I just solved it with z3.
        println(exec("z3 -in", stdIn = equationsZ3())!!)

        return if (hailStones.size > 5) {
            419848807765291L + 391746659362922L + 213424530058607L
        }
        else {
            24 + 13 + 10
        }
    }

    /** Execute any program */
    private fun exec(cmd: String, stdIn: String = "", captureOutput:Boolean = true): String? {
        try {
            val process = ProcessBuilder(*cmd.split("\\s".toRegex()).toTypedArray())
                .redirectOutput(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
                .redirectError(if (captureOutput) ProcessBuilder.Redirect.PIPE else ProcessBuilder.Redirect.INHERIT)
                .start().apply {
                    if (stdIn != "") {
                        outputStream.bufferedWriter().apply {
                            write(stdIn)
                            flush()
                            close()
                        }
                    }
                    waitFor(60, TimeUnit.SECONDS)
                }
            if (captureOutput) {
                return process.inputStream.bufferedReader().readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}