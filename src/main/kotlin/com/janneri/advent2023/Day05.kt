// See puzzle in https://adventofcode.com/2023/day/5
package com.janneri.advent2023

import com.janneri.advent2023.util.parseLongs

private fun LongRange.intersects(other: LongRange) =
    other.contains(this.first) || other.contains(this.last) || this.contains(other.first) || this.contains(other.last)

private fun LongRange.intersectingRange(other: LongRange): LongRange =
    LongRange(kotlin.math.max(this.first, other.first), kotlin.math.min(this.last, other.last))

private fun LongRange.shift(amount: Long): LongRange = LongRange(this.first + amount, this.last + amount)

class Day05(inputLines: List<String>) {
    private val seeds: Set<Long> = parseLongs(inputLines.first().substringAfter(": ")).toSet()

    private fun parseMap(lines: List<String>): GardenMap {
        val mapName = lines.first()
        val mapperFunctions = lines.drop(1).map { line ->
            parseLongs(line).let { (destStart, srcStart , size) ->
                MapperFunction(
                    LongRange(srcStart, srcStart + size - 1),
                    LongRange(destStart, destStart + size - 1),
                )
            }
        }

        return GardenMap(mapName, mapperFunctions)
    }

    private val gardenMaps = inputLines.drop(2).joinToString("\n").split("\n\n")
        .map { parseMap(it.split("\n")) }

    data class MapperFunction(val srcRange: LongRange, val destRange: LongRange) {
        fun apply(seed: Long) = destRange.first + (seed - srcRange.first)
        fun contains(seed: Long) = srcRange.contains(seed)
    }

    data class MatchResult(val matchedRanges: Set<LongRange>, val unmatchedRanges: Set<LongRange>) {
        fun matchedAndUnMatchedRanges(): Set<LongRange> = matchedRanges + unmatchedRanges
    }

    data class GardenMap(val name: String, val mappers: List<MapperFunction>) {

        private fun seedRangeToAllPossibleDestRanges(seedRange: LongRange, srcRange: LongRange, destRange: LongRange): MatchResult {
            val matchedRanges: MutableList<LongRange> = mutableListOf()
            val unmatchedRanges: MutableList<LongRange> = mutableListOf()

            if (!seedRange.intersects(srcRange)) {
                unmatchedRanges.add(seedRange)
            }
            else {
                if (seedRange.first < srcRange.first) {
                    unmatchedRanges.add(LongRange(seedRange.first, srcRange.first - 1))
                }
                if (seedRange.last > srcRange.last) {
                    unmatchedRanges.add(LongRange(srcRange.last + 1, seedRange.last))
                }

                matchedRanges.add(seedRange.intersectingRange(srcRange).shift(destRange.first - srcRange.first))
            }

            return MatchResult(matchedRanges.toSet(), unmatchedRanges.toSet())
        }

        private fun allPossibleRanges(matchResult: MatchResult): MatchResult =
            mappers.fold(matchResult) { currentResult, mapperFunction ->
                val newMatchResults = currentResult.unmatchedRanges.map { fromRange ->
                    seedRangeToAllPossibleDestRanges(fromRange, mapperFunction.srcRange, mapperFunction.destRange)
                }
                MatchResult(
                    currentResult.matchedRanges + newMatchResults.flatMap { it.matchedRanges }.toSet(),
                    newMatchResults.flatMap { it.unmatchedRanges }.toSet(),
                )
            }

        fun allPossibleRanges(fromRanges: Set<LongRange>): Set<LongRange> =
            allPossibleRanges(MatchResult(emptySet(), fromRanges)).matchedAndUnMatchedRanges()

        fun seedVal(seed: Long): Long = mappers
            .find { it.srcRange.contains(seed) }?.apply(seed) ?: seed
    }

    private fun seedToLocation(seed: Long): Long =
        gardenMaps.fold(seed) { acc, gardenMap -> gardenMap.seedVal(acc) }

    fun part1(): Long = seeds.minOfOrNull { seedToLocation(it) } ?: error("no seeds")

    fun part2(): Long {
        val seedRanges = seeds.chunked(2)
            .map { LongRange(it[0], it[0] + it[1] - 1) }
            .sortedBy { it.first }
            .toSet()

        val allPossibleEndRanges = gardenMaps.fold(seedRanges) { acc, gardenMap ->
            gardenMap.allPossibleRanges(acc)
        }

        return allPossibleEndRanges.minOfOrNull { it.first } ?: error("no seeds")
    }

}
