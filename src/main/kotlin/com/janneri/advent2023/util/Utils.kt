package com.janneri.advent2023.util

import java.nio.file.Path

fun readInput(name: String) = Path.of("src", "test", "resources", "$name.txt").toFile()
    .readLines()

infix fun <E> Set<E>.overlaps(otherSet: Set<E>): Boolean = this.any { otherSet.contains(it) }


private val numPattern = """-?[0-9]+""".toRegex()
fun parseInts(str: String): List<Int> =
    numPattern.findAll(str).map { it.value.toInt() }.toList()

fun parseUlongs(str: String): List<ULong> =
    numPattern.findAll(str).map { it.value.toULong() }.toList()

fun parseLongs(str: String): List<Long> =
    numPattern.findAll(str).map { it.value.toLong() }.toList()


enum class Direction(val dx: Int, val dy: Int, val symbol: Char) {
    UP(0, -1, '^') {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
    },
    RIGHT(1, 0, '>') {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
    },
    DOWN(0, 1, 'v') {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
    },
    LEFT(-1, 0, '<') {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    fun turnOpposite() = turnLeft().turnLeft()
}

enum class IDirection(val dx: Int, val dy: Int) {
    NORTH(0, -1),
    NORTHEAST(1, -1),
    EAST(1, 0),
    SOUTHEAST(1, 1),
    SOUTH(0, 1),
    SOUTHWEST(-1, 1),
    WEST(-1, 0),
    NORTHWEST(-1, -1)
}

data class Coord(val x: Int, val y: Int) {
    fun neighbors(): List<Coord> = Direction.values().map { this.move(it) }
    fun neighborsIncludingDiagonal(): Set<Coord> = IDirection.values().map { this.move(it) }.toSet()
    fun move(direction: IDirection, amount: Int = 1) =
        Coord(x + amount * direction.dx, y + amount * direction.dy)

    fun move(direction: Direction, amount: Int = 1) =
        Coord(x + amount * direction.dx, y + amount * direction.dy)

    fun moveUntil(direction: Direction, predicate: (Coord) -> Boolean): Coord {
        val maxMoves = 10000
        var newCoord = this

        for (n in 0 until maxMoves) {
            val potentialNewCoord = newCoord.move(direction)
            if (predicate(potentialNewCoord)) {
                return newCoord
            }
            newCoord = potentialNewCoord
        }

        throw IllegalArgumentException("Too many (> $maxMoves) moves!")
    }

    override fun toString() = "($x, $y)"
}

typealias Path = List<Coord>

fun drawGrid(coords: Set<Coord>, tileSymbolAt: (Coord) -> Char) {
    val yRange = coords.minBy { it.y }.y..coords.maxBy { it.y }.y
    val xRange = coords.minBy { it.x }.x..coords.maxBy { it.x }.x

    for (y in yRange) {
        for (x in xRange) {
            val coord = Coord(x, y)
            print(tileSymbolAt(coord))
        }
        println()
    }
}