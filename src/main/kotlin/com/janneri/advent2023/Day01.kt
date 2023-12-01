// See puzzle in https://adventofcode.com/2023/day/1
package com.janneri.advent2023

class Day01(val inputLines: List<String>) {
    private val digitNames = mapOf(
        1 to "one",
        2 to "two",
        3 to "three",
        4 to "four",
        5 to "five",
        6 to "six",
        7 to "seven",
        8 to "eight",
        9 to "nine"
    )

    fun part1(): Int = inputLines
            .map { it.filter { char -> char.isDigit() } }
            .map { it.first() + "" + it.last() }
            .sumOf { it.toInt() }

    fun part2(): Int {
        fun collectDigits(str: String): List<Pair<Int, Int>> {
            val result = mutableListOf<Pair<Int, Int>>()
            (1..9).forEach { digit ->
                val index = str.indexOf(digit.toString())
                val indexLast = str.lastIndexOf(digit.toString())
                val nameIndex = str.indexOf(digitNames[digit]!!)
                val nameIndexLast = str.lastIndexOf(digitNames[digit]!!)
                if (index != -1) result.add(digit to index)
                if (index != -1 && index != indexLast) result.add(digit to indexLast)
                if (nameIndex != -1) result.add(digit to nameIndex)
                if (nameIndex != -1 && nameIndex != nameIndexLast) result.add(digit to nameIndexLast)
            }
            return result.sortedBy { (_, index) -> index }
        }

        return inputLines
            .map {
                val allDigits = collectDigits(it)
                allDigits.first().first.toString() + "" + allDigits.last().first.toString()
            }
            .sumOf { it.toInt() }
    }
}