package com.janneri.advent2023.util

import java.nio.file.Path
import kotlin.math.abs

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

fun IntRange.intersectRange(other: IntRange) = (maxOf(first, other.first)..minOf(last, other.last))

enum class Direction(val dx: Int, val dy: Int, val symbol: Char, val letter: Char) {
    UP(0, -1, '^', 'U') {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
    },
    RIGHT(1, 0, '>', 'R') {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
    },
    DOWN(0, 1, 'v', 'D') {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
    },
    LEFT(-1, 0, '<', 'L') {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
    };

    abstract fun turnLeft(): Direction
    abstract fun turnRight(): Direction
    fun turnOpposite() = turnLeft().turnLeft()

    companion object {
        fun ofLetter(letter: Char): Direction =
            values().find { it.letter == letter } ?: error("invalid letter")
    }
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

data class LongCoord(val x: Long, val y: Long) {
    fun distanceTo(c: LongCoord): Long = abs(this.x - c.x) + abs(this.y - c.y)

    fun move(direction: Direction, amount: Long = 1) =
        LongCoord(x + amount * direction.dx, y + amount * direction.dy)
}

data class Coord(val x: Int, val y: Int): Comparable<Coord> {
    fun neighbors(): List<Coord> = Direction.values().map { this.move(it) }
    fun neighborsIncludingDiagonal(): Set<Coord> = IDirection.values().map { this.move(it) }.toSet()

    fun distanceTo(c: Coord): Int = abs(this.x - c.x) + abs(this.y - c.y)

    fun directionTo(c: Coord): Direction = when {
        x < c.x -> Direction.RIGHT
        x > c.x -> Direction.LEFT
        y < c.y -> Direction.DOWN
        else -> Direction.UP
    }

    fun move(direction: IDirection, amount: Int = 1) =
        Coord(x + amount * direction.dx, y + amount * direction.dy)

    fun move(direction: Direction, amount: Int = 1) =
        Coord(x + amount * direction.dx, y + amount * direction.dy)

    fun moveCollect(direction: Direction, amount: Int = 1): List<Coord> =
        (0 .. amount).map { steps ->
            this.move(direction, steps)
        }

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

    override fun compareTo(other: Coord) = compareValuesBy(this, other,
        { it.y },
        { it.x }
    )
}

typealias Path = List<Coord>

infix fun <T> Any.log(n: Int): T {
    if (n == 1) println(this)
    return this as T
}

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