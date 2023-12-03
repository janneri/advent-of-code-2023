// See puzzle in https://adventofcode.com/2023/day/3
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord

class Day03(private val inputLines: List<String>) {
    private val numberRegex = Regex("[0-9]+")
    data class PartNumber(val value: Int, val coords: Set<Coord>)
    private val partNumbers = mutableListOf<PartNumber>()

    private val symbolCoords = mutableSetOf<Coord>()
    private val gearCoords = mutableSetOf<Coord>()
    private fun isSymbol(char: Char) = !char.isDigit() && char != '.'

    private fun collectNumbersAndSymbols() {
        inputLines.forEachIndexed { y, str ->
            partNumbers.addAll(numberRegex.findAll(str)
                .map { PartNumber(it.value.toInt(), it.range.map { x -> Coord(x, y) }.toSet()) })

            str.forEachIndexed { x, char ->
                if (isSymbol(char)) symbolCoords.add(Coord(x, y))
                if ('*' == char) gearCoords.add(Coord(x, y))
            }
        }
    }

    init {
        collectNumbersAndSymbols()
    }

    private fun isAdjacentToSymbol(partNumber: PartNumber): Boolean =
        partNumber.coords.any { coord -> coord.diagonalNeighbors().any { symbolCoords.contains(it) } }

    fun part1(): Int = partNumbers.filter { isAdjacentToSymbol(it) }.sumOf { it.value }

    fun part2(): Int {
        val gearRatios = gearCoords.map { gearCoord ->
            val parts = partNumbers.filter { partNumber ->
                partNumber.coords.any { gearCoord.diagonalNeighbors().contains(it) }
            }
            when {
                parts.size >= 2 -> parts.fold(1) { acc, partNumber ->  acc * partNumber.value}
                else -> 0
            }
        }
        return gearRatios.sum()
    }
}