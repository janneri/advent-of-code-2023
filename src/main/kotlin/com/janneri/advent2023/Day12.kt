// See puzzle in https://adventofcode.com/2023/day/12
package com.janneri.advent2023

import com.janneri.advent2023.util.parseInts


class Day12(val inputLines: List<String>) {

    fun List<Int>.repeat(n: Int): List<Int> {
        val result = mutableListOf<Int>()
        repeat(n) { result.addAll(this) }
        return result.toList()
    }

    data class ConditionRecord(val str: String,
                               val groupSizes: List<Int>,
                               val repeatedStr: String,
                               val repeatedGroupSizes: List<Int>) {
        companion object {
            fun of(str: String): ConditionRecord {
                val (rStr, gStr) = str.split(" ")
                val repeatedStr = "$rStr?".repeat(5).dropLast(1)
                val repeatedGStr = "$gStr,".repeat(5).dropLast(1)
                return ConditionRecord(rStr, parseInts(gStr), repeatedStr, parseInts(repeatedGStr))
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


    fun findPermutations(str: String, groupSizes: List<Int>): Int {
        val len = str.count { it == '?' }
        val maxBits = "1".repeat(len)
        val maxNum = maxBits.toInt(2)
        var results = mutableListOf<String>()
        val permutations = (0..maxNum).filter { num ->
            val bitStr = num.toBitString(len)
            val newRec = replaceQuestionMarks(str, bitStr)
            val newGroupSizes = groupSizes(newRec)
            val matches = groupSizes == newGroupSizes
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

    fun part1(): Int = records.sumOf { findPermutations(it.str, it.groupSizes) }



    fun part2(): ULong {
        return records.sumOf { solve(it.repeatedStr.toCharArray().toList(), it.repeatedGroupSizes, null) }
    }

    data class CacheKey(val chars: List<Char>, val remainingSizes: List<Int>, val currentCount: Int?)
    val cache = mutableMapOf<CacheKey, ULong>()

    fun solve(chars: List<Char>, remainingSizes: List<Int>, currentCount: Int?): ULong {
//        println("chars: " + chars.joinToString() + "     sizes: " + remainingSizes.joinToString(",") + "     currentCount: " + currentCount)
        if (chars.isEmpty()) {
            if (currentCount == null && remainingSizes.isEmpty()) return 1u
            if (remainingSizes.size == 1 && currentCount == remainingSizes[0]) return 1u
            return 0u
        }

        val possibleMore = chars.count { it == '#' || it == '?' }

        // not enough chars left to find enough springs
        if (currentCount != null && possibleMore + currentCount < remainingSizes.sum()) return 0u
        if (currentCount == null && possibleMore < remainingSizes.sum()) return 0u
        // not possible to find more groups
        if (currentCount != null && remainingSizes.isEmpty()) return 0u

        val cacheKey = CacheKey(chars, remainingSizes, currentCount)
        if (cache.contains(cacheKey)) return cache.get(cacheKey)!!
        
        var possibleCount = (0).toULong()

        // match is not found, because the current spring count does not match the required group size
        if (chars[0] == '.' && currentCount != null && currentCount != remainingSizes.first()) return 0u
        // a group is found, lets drop 1 from the remaining groups and reset spring count
        if (chars[0] == '.' && currentCount != null) possibleCount += solve(chars.drop(1), remainingSizes.drop(1), null)
        // a group is found (if ? is .) because it matches the expected group size
        if (chars[0] == '?' && currentCount != null && currentCount == remainingSizes[0]) possibleCount += solve(chars.drop(1), remainingSizes.drop(1), null)
        // increase the current count
        if (chars[0] in "?#" && currentCount != null) possibleCount += solve(chars.drop(1), remainingSizes, currentCount + 1)
        // set current count to 1 (because a new group starts)
        if (chars[0] in "?#" && currentCount == null) possibleCount += solve(chars.drop(1), remainingSizes, 1)
        // just skip the char (because it is .)
        if (chars[0] in "?." && currentCount == null) possibleCount += solve(chars.drop(1), remainingSizes, null)

        cache.put(cacheKey, possibleCount)
        return possibleCount
    }
}

