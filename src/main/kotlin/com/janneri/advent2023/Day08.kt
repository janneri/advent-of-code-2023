// See puzzle in https://adventofcode.com/2023/day/8
package com.janneri.advent2023

import java.math.BigInteger

class Day08(val inputLines: List<String>) {
    private val moves = inputLines.first().toCharArray()

    private val nodes = inputLines.drop(2).associate { str ->
        str.substring(0, 3) to (str.substring(7, 10) to str.substring(12, 15))
    }

    private fun getStepsToEnd(start: String, isEnd: (String) -> Boolean): Int {
        var count = 0
        var currentNode = start
        do {
            val (left, right) = nodes[currentNode] ?: error("node not found")
            currentNode = if (moves[count++ % moves.size] == 'L') left else right
        } while (!isEnd(currentNode))
        return count
    }

    fun part1(): Int = getStepsToEnd("AAA") { it == "ZZZ"}

    fun part2(): BigInteger = nodes.keys.filter { location -> location.last() == 'A' }
        .map { start -> getStepsToEnd(start) { location -> location.endsWith('Z') }.toBigInteger() }
        // Assume it takes 8 steps in path 1 and 12 steps in path 2. The greatest common divisor is 4.
        // So we end up with 8*12/4=24 steps to have both paths simultaneously end in nodes that end with Z.
        .reduce { acc, stepCount -> acc * stepCount / acc.gcd(stepCount) }

}