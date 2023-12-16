// See puzzle in https://adventofcode.com/2023/day/15
package com.janneri.advent2023

class Day15(val inputLines: List<String>) {
    private data class Step(val label: String,
                            val operation: Char,
                            val labelHash: Int,
                            val fullHash: Int,
                            val focalLength: Int?)

    private fun hash(str: String) =
        str.fold(0) { acc: Int, c: Char -> ((acc + c.code) * 17) % 256 }

    private fun parseStep(str: String): Step {
        val fullHash = hash(str)

        return if (str.contains("=")) {
            str.split("=").let {
                Step(it[0], '=', hash(it[0]), fullHash, it[1].toInt())
            }
        }
        else {
            val label = str.dropLast(1)
            Step(label, '-', hash(label), fullHash, null)
        }
    }

    private val steps = inputLines.first().split(",").map { parseStep(it) }

    fun part1(): Int = steps.sumOf {it.fullHash}

    fun part2(): Int {
        val boxes = (0..255).map { mutableMapOf<String, Int>() }
        steps.forEach { step ->
            val box = boxes[step.labelHash]
            if (step.operation == '-') box.remove(step.label)
            if (step.operation == '=') box[step.label] = step.focalLength!!
        }

        return boxes.foldIndexed(0) { boxNum, totalFocusPower, box ->
            totalFocusPower + (1 + boxNum) * box.values.foldIndexed(0) { lensIdx, lensPower, lensFocalLen ->
                lensPower + (lensIdx + 1) * lensFocalLen
            }
        }
    }
}