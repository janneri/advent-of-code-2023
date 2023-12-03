// See puzzle in https://adventofcode.com/2023/day/3
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.overlaps


class Day03(private val inputLines: List<String>) {
    data class Symbol(val char: Char, val position: Coord)

    data class PartNumber(val value: Int, val coords: Set<Coord>) {
        fun isAdjacentTo(symbol: Symbol): Boolean =
            this.coords overlaps symbol.position.neighborsIncludingDiagonal()
        fun isAdjacentToAny(symbols: Set<Symbol>): Boolean = symbols.any { this.isAdjacentTo(it) }
    }

    private val partNumbers = mutableListOf<PartNumber>()
    private val symbols = mutableSetOf<Symbol>()

    private fun collectNumbersAndSymbols() {
        val numberRegex = Regex("[0-9]+")
        fun isSymbol(char: Char) = !char.isDigit() && char != '.'

        inputLines.forEachIndexed { y, str ->
            partNumbers.addAll(numberRegex.findAll(str)
                .map { PartNumber(it.value.toInt(), it.range.map { x -> Coord(x, y) }.toSet()) })

            str.forEachIndexed { x, char ->
                if (isSymbol(char)) symbols.add(Symbol(char, Coord(x, y)))
            }
        }
    }

    init {
       collectNumbersAndSymbols()
    }

    fun part1(): Int = partNumbers.filter { it.isAdjacentToAny(symbols) }.sumOf { it.value }

    fun part2(): Int {
        val gearRatios = symbols.filter { it.char == '*' }.map { gearSymbol ->
            val parts = partNumbers.filter { it.isAdjacentTo(gearSymbol) }
            when {
                parts.size >= 2 -> parts.fold(1) { acc, partNumber ->  acc * partNumber.value}
                else -> 0
            }
        }
        return gearRatios.sum()
    }
}