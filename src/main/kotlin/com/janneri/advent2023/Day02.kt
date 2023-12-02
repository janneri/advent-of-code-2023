// See puzzle in https://adventofcode.com/2023/day/2
package com.janneri.advent2023

class Day02(inputLines: List<String>) {
    private val games = inputLines.map { Game.of(it) }

    data class Cube(val color: String, val amount: Int) {
        companion object {
            fun of(str: String): Cube {
                val parts = str.trim().split(" ")
                return Cube(parts[1], parts[0].toInt())
            }
        }
    }

    data class CubeSet(val cubes: List<Cube>) {
        companion object {
            fun of(str: String): CubeSet {
                val parts = str.split(", ")
                return CubeSet(parts.map { Cube.of(it) })
            }
        }

        fun amount(color: String): Int = cubes.find { it.color == color }?.amount ?: 0
    }

    data class Game(val num: Int, val cubeSets: List<CubeSet>) {
        companion object {
            private val pattern = """^Game (\d+): (.+)$""".toRegex()
            fun of(input: String): Game {
                val (numStr, cubeSetStr) = pattern.find(input)!!.destructured
                return Game(numStr.toInt(), cubeSetStr.split(";").map { CubeSet.of(it) })
            }
        }

        fun maxColorAmount(color: String): Int = cubeSets.map { it.amount(color) }.max()
    }

    fun part1(): Int = games.filter {
        it.maxColorAmount("red") <= 12 &&
        it.maxColorAmount("green") <= 13 &&
        it.maxColorAmount("blue") <= 14
    }.sumOf { it.num }

    fun part2(): Int = games.sumOf {
        it.maxColorAmount("red") *
        it.maxColorAmount("green") *
        it.maxColorAmount("blue")
    }
}