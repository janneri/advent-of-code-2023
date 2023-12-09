// See puzzle in https://adventofcode.com/2023/day/9
package com.janneri.advent2023

class Day09(val inputLines: List<String>) {
    private val sequences = inputLines.map { str -> str.split(" ").map { it.toInt() } }

    private fun predictFirstAndLast(forSequence: List<Int>): Pair<Long, Long> {
        val diffSeqs = mutableListOf(forSequence)
        do {
            val curSeq = diffSeqs.last()
            val newDiffSeq = curSeq.drop(1).mapIndexed { idx, num -> (num - curSeq[idx]) }
            diffSeqs.add(newDiffSeq)
        } while (newDiffSeq.any { it != 0 })

        diffSeqs.reverse()

        val lastNum = diffSeqs.fold(0L) { acc, seq -> acc + seq.last() }
        val firstNum = diffSeqs.fold(0L) { acc, seq -> seq.first() - acc }

        return firstNum to lastNum
    }

    fun part1(): Long = sequences.sumOf { predictFirstAndLast(it).second }

    fun part2(): Long = sequences.sumOf { predictFirstAndLast(it).first }
}