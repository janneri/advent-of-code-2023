// See puzzle in https://adventofcode.com/2023/day/5
package com.janneri.advent2023

import com.janneri.advent2023.util.readInput

private fun LongRange.intersects(other: LongRange) =
    other.contains(this.first) || other.contains(this.last) || this.contains(other.first) || this.contains(other.last)

private fun LongRange.intersectingRange(other: LongRange): LongRange =
    LongRange(kotlin.math.max(this.first, other.first), kotlin.math.min(this.last, other.last))

private fun LongRange.shift(amount: Long): LongRange = LongRange(this.first + amount, this.last + amount)

class Day05(inputLines: List<String>) {
    private val numPattern = """[0-9]+""".toRegex()
    private fun parseNums(str: String): List<Long> =
        numPattern.findAll(str).map { it.value.toLong() }.toList()

    data class MatchResult(val matchedRanges: List<LongRange>, val unmatchedRanges: List<LongRange>)

    data class GardenMap(val name: String, val seedMap: List<List<Long>>) {
        val destRanges = seedMap.map { LongRange(it[0], it[0] + it[2] - 1) }
        val srcRanges = seedMap.map { LongRange(it[1], it[1] + it[2] - 1) }
        val mappers = srcRanges.mapIndexed { index, srcRange -> MapperFunction(srcRange, destRanges[index]) }

        fun seedRangeToAllPossibleDestRanges(seedRange: LongRange, srcRange: LongRange, destRange: LongRange): MatchResult {
            val matchedRanges: MutableList<LongRange> = mutableListOf()
            val unmatchedRanges: MutableList<LongRange> = mutableListOf()

            if (!seedRange.intersects(srcRange)) {
                unmatchedRanges.add(seedRange)
            }
            else {
                if (seedRange.first < srcRange.first) {
                    matchedRanges.add(LongRange(seedRange.first, srcRange.first - 1))
                }
                if (seedRange.last > srcRange.last) {
                    matchedRanges.add(LongRange(srcRange.last + 1, seedRange.last))
                }

                matchedRanges.add(seedRange.intersectingRange(srcRange).shift(destRange.first - srcRange.first))
            }

            return MatchResult(matchedRanges, unmatchedRanges)
        }

        fun allPossibleRanges(fromRanges: Set<LongRange>): Set<LongRange> {
            val resultRanges = mutableSetOf<LongRange>()

            fromRanges.forEach { seedRange ->
                val unmappedRanges = mutableSetOf<LongRange>()
                val mappedRanges = mutableSetOf<LongRange>()
                srcRanges.foldIndexed(fromRanges) { idx, acc, srcRange ->
                    val resultRanges = seedRangeToAllPossibleDestRanges(seedRange, srcRange, destRanges[idx])
                    mappedRanges.addAll(resultRanges.matchedRanges)
                    // only the unmapped values should be given forward to the next mapper function

                    acc

//                    if (!seedRange.intersects(srcRange)) {
//                        unmappedRanges.add(seedRange)
//                    }
//                    else {
//                        val ranges = seedRangeToAllPossibleDestRanges(seedRange, srcRange, destRanges[idx])
//                        if (ranges.size == 1) {
//                            mappedRanges.add(ranges.first())
//                            println("from $seedRange, src $srcRange to fully mapped $ranges")
//                        }
//                        else {
//                            println("from $seedRange, src $srcRange dest ${destRanges[idx]} to other $ranges")
//                            unmappedRanges.addAll(ranges)
//                        }
//                    }
                    // todo the seedRange hits, it should be removed
                    // 55..67 is fully matched by 50..97
                    // 55..67 is not matched by 98..99
//                    println("from $seedRange, $srcRange, ${destRanges[idx]} to $foo")
//                    foo
                }
                if (mappedRanges.isNotEmpty()) {
                    resultRanges.addAll(mappedRanges)
                }
                else {
                    resultRanges.addAll(unmappedRanges)
                }
                println("from $seedRange, to $resultRanges")
            }
            return resultRanges
        }

        fun seedVal(seed: Long): Long {
            var rangeIndex: Int? = null
            srcRanges.forEachIndexed { idx, range -> if (range.contains(seed)) rangeIndex = idx }

            if (rangeIndex == null) {
                return seed
            }
            else {
                val srcRange = srcRanges[rangeIndex!!]
                val destRange = destRanges[rangeIndex!!]
                return destRange.first + (seed - srcRange.first)
            }
        }
    }

    data class MapperFunction(val srcRange: LongRange, val destRange: LongRange) {
        fun apply(seed: Long) = destRange.first + (seed - srcRange.first)
        fun contains(seed: Long) = srcRange.contains(seed)

        fun reverse() = MapperFunction(destRange, srcRange)
    }

    private val seeds: Set<Long> = parseNums(inputLines.first().substringAfter(": ")).toSet()

    private fun parseMap(lines: List<String>): GardenMap =
        GardenMap(lines.first(), lines.drop(1).map { parseNums(it) })

    val maps = inputLines.drop(2).joinToString("\n").split("\n\n").map { parseMap(it.split("\n")) }

    private fun seedToLocation(seed: Long): Long {
        var current = seed
        maps.forEach { current = it.seedVal(current) }
        return current
    }

    fun part1(): Long = seeds.map {seedToLocation(it)}.min()

    fun part2(): Long {
        val seedRanges = seeds.chunked(2)
            .map { LongRange(it[0], it[0] + it[1] - 1) }
            .sortedBy { it.first }
            .toSet()

        println("seedRanges $seedRanges")
        println(maps[0].allPossibleRanges(seedRanges))
//        println(maps[0].seedRangeToAllPossibleDestRanges(LongRange(55, 67), LongRange(50, 97), LongRange(52, 99)))
//        println(maps[0].seedRangeToAllPossibleDestRanges(LongRange(79, 92), LongRange(50, 97), LongRange(52, 99)))

        return 0
//        val allRanges = maps.fold(seedRanges) { acc, gardenMap ->
//            println(gardenMap)
//            val newRanges = gardenMap.allPossibleRanges(acc)
//            println(newRanges.sortedBy { it.first })
//            newRanges
//        }
//        println("all ranges size ${allRanges.size}")
//        return allRanges.map { it.first }.min()
    }

    fun part2BruteForce(): Int {
        val seedRanges = seeds.chunked(2).map { LongRange(it[0], it[0] + it[1] - 1) }.sortedBy { it.first }
        println("${seedRanges[0]}: ${seedRanges[0].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[1]}: ${seedRanges[1].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[2]}: ${seedRanges[2].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[3]}: ${seedRanges[3].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[4]}: ${seedRanges[4].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[5]}: ${seedRanges[5].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[6]}: ${seedRanges[6].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[7]}: ${seedRanges[7].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[8]}: ${seedRanges[8].minOfOrNull { seedToLocation(it) }}")
        println("${seedRanges[9]}: ${seedRanges[9].minOfOrNull { seedToLocation(it) }}")
        return 0
    }

    fun part2ReverseTactic(): Long {
        val seedRanges =
            seeds.chunked(2).map { LongRange(it[0], it[0] + it[1] - 1) }.sortedBy { it.first }

        val gmap = maps.first()
        val foo = MapperFunction(gmap.srcRanges.first(), gmap.destRanges.first())

        println(foo.apply(79))
        println(foo.reverse().apply(31))
        val smallestSeed = LongRange(1, 100).firstOrNull { seed ->
            val resultSeed = 1L
            maps.reversed().fold(seed) { acc, gardenMap ->
                gardenMap.mappers.reversed().forEach { mapper ->
                    val reverseMapper = mapper.reverse()
                    if (reverseMapper.contains(seed)) reverseMapper.apply(seed) else seed
                }
                acc
            }
            seedRanges.any { it.contains(resultSeed) }
        }
        return smallestSeed ?: 0
    }
}

fun main() {
    val result = Day05(readInput("Day05_test")).part2ReverseTactic()
    println(result)
}

/*
  63627802.. 342739752 first:  747113782 last: 2895443503  lowest:  183085156
 546703948.. 670481658 first: 2265106447 last:  825737351  lowest:  800883183
 950527520..1035708719 first: 1847272394 last: 3090071033  lowest:  702443113
1141059215..1387526139 first: 2939770087 last: 3427763388  lowest: 1069644087
1655973293..1754184218 first: 3229630316 last: 3487938039  lowest: 2491050754
2009732824..2334892580 first: 1257121020 last: 3674676745  lowest:  650827277
2424412143..2672147550 first: 2689967236 last: 4070233002  lowest:  125742456
3575518161..3945632408 first: 3356199914 last: 3510120385  lowest:  919469344
3948361820..4041166329 first: 3014246475 last: 2415857356  lowest: 2355386760
4140139679..4222712325 first: 1378139923 last: 1181276120  lowest: 1169277931
 */
