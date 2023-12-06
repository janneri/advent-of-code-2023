// See puzzle in https://adventofcode.com/2023/day/6
package com.janneri.advent2023

import com.janneri.advent2023.util.parseUlongs

class Day06(inputLines: List<String>) {
    private val times = parseUlongs(inputLines.first().substringAfter(":"))
    private val dists = parseUlongs(inputLines.last().substringAfter(":"))
    data class Race(val time: ULong, val dist: ULong)
    private val races = times.mapIndexed { index, time -> Race(time, dists[index]) }
    private val part2Race = Race(
        times.joinToString("").toULong(),
        dists.joinToString("").toULong(),
    )

    private fun dist(buttonTime: ULong, maxTime: ULong): ULong {
        val movingTime = maxTime - buttonTime
        return buttonTime * movingTime // buttonTime = speed
    }

    private fun winningDists(maxTime: ULong, distToBeat: ULong): List<ULong> = ULongRange(1u, maxTime - 1u)
        .map { dist(it, maxTime) }
        .filter { it > distToBeat }

    private fun product(nums: List<Int>): Int = nums.fold(1) { acc, count -> acc * count}

    fun part1(): Int = product(races.map { winningDists(it.time, it.dist).size })

    private fun findTippingPoint(start: ULong, step: Int): ULong {
        var current = start
        do {
            val win = dist(current, part2Race.time) > part2Race.dist
            when {
                step < 0 -> current -= 1u
                else -> current += 1u
            }
        } while (!win)
        return current
    }

    fun part2(): ULong {
        val winStart = findTippingPoint(1u, 1) - 1u
        val winEnd = findTippingPoint(part2Race.time, -1) + 1u
        println("winning range $winStart - $winEnd")
        return winEnd - winStart + 1u
    }
}