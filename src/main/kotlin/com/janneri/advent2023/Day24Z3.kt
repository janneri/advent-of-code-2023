// See puzzle in https://adventofcode.com/2023/day/24
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord3d
import com.janneri.advent2023.util.parseLongs
import com.janneri.advent2023.util.readInput
import java.io.IOException
import java.util.concurrent.TimeUnit

class Day24Z3(inputLines: List<String>) {
    private val hailStones = inputLines.map { Hailstone.parse(it) }

    private data class Hailstone(val pos: Coord3d, val velocity: Coord3d) {
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

    fun equationsZ3(): String {
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

    fun solve() {
        // Too lazy, so I just solved it with z3.
        println(exec("z3 -in", stdIn = equationsZ3())!!)
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

fun main() {
    val solver = Day24Z3(readInput("Day24"))
    solver.solve()
}