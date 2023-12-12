// See puzzle in https://adventofcode.com/2023/day/12
package com.janneri.advent2023

import com.janneri.advent2023.util.parseInts



class Day12(val inputLines: List<String>) {
    data class ConditionRecord(val str: String, val groupSizes: List<Int>) {
        companion object {
            fun of(str: String): ConditionRecord {
                val (rStr, gStr) = str.split(" ")
                return ConditionRecord(rStr, parseInts(gStr))
            }
        }
    }
    val records = inputLines.map { ConditionRecord.of(it) }

    private fun permutations(len: Int): List<String> {
        val maxBits = "1".repeat(len)
        val maxNum = maxBits.toInt(2)
        return (0..maxNum).map { it.toBitString(len) }
    }

    fun replaceQuestionMarks(record: String, bitStr: String): String =
        record.fold(0 to "") { acc, char ->
            val (idx, currentStr) = acc
            if (char == '?') {
                (idx + 1) to currentStr + (if (bitStr[idx] == '1') '#' else '.')
            }
            else {
                idx to (currentStr + char)
            }
        }.second

    private fun Int.toBitString(len: Int): String =
        Integer.toBinaryString(this).padStart(len, '0')


    fun findPermutations(record: ConditionRecord): Int {
        val len = record.str.count { it == '?' }
        val maxBits = "1".repeat(len)
        val maxNum = maxBits.toInt(2)
        var results = mutableListOf<String>()
        val permutations = (0..maxNum).filter { num ->
            val bitStr = num.toBitString(len)
            val newRec = replaceQuestionMarks(record.str, bitStr)
            val matches = record.groupSizes == groupSizes(newRec)
            if (matches) results.add(newRec)
            matches
        }
        println(results)
        return permutations.size
    }

    fun groupSizes(record: String): List<Int> =
        record.fold(false to mutableListOf<Int>()) { acc, char ->
            val (adding, currentList) = acc
            if (char == '#') {
                if (adding) currentList[currentList.lastIndex] = currentList.last() + 1
                else currentList.add(1)
            }

            (char == '#') to currentList
        }.second

    fun part1(): Int = records.sumOf { findPermutations(it) }

    fun part2(): Int {
        val foo = "#.#.### 1,1,3"
        return 0
    }
}