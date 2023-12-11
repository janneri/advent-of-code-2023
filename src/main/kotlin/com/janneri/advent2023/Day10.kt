// See puzzle in https://adventofcode.com/2023/day/10
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord
import com.janneri.advent2023.util.Direction
import com.janneri.advent2023.util.Direction.*

class Day10(val inputLines: List<String>) {
    private var start = Coord(0, 0)
    private val coords = inputLines.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            if (char == 'S') start = Coord(x, y)
            Coord(x, y)
        }
    }.toSet()
    private val height = inputLines.size
    private val width = inputLines.first().length

    private fun outofBounds(coord: Coord) =
        coord.x < 0 || coord.x >= width || coord.y < 0 || coord.y >= height

    private fun tileAt(coord: Coord): Char = when {
        outofBounds(coord) -> '#'
        else -> inputLines[coord.y][coord.x]
    }

    private fun moveableDirections(fromCoord: Coord): List<Direction> {
        val result = mutableListOf<Direction>()
        if (tileAt(fromCoord.move(UP)) in "7|F") result.add(UP)
        if (tileAt(fromCoord.move(RIGHT)) in "7-J") result.add(RIGHT)
        if (tileAt(fromCoord.move(DOWN)) in "L|J") result.add(DOWN)
        if (tileAt(fromCoord.move(LEFT)) in "L-F") result.add(LEFT)

        return result
    }

    private fun move(from: Pair<Direction, Coord>): Pair<Direction, Coord> {
        val (direction, fromCoord) = from
        val newCoord = fromCoord.move(direction)
        val newDirection = when {
            direction == UP && tileAt(newCoord) == 'F' -> RIGHT
            direction == LEFT && tileAt(newCoord) == 'F' -> DOWN
            direction == RIGHT && tileAt(newCoord) == '-' -> RIGHT
            direction == LEFT && tileAt(newCoord) == '-' -> LEFT
            direction == UP && tileAt(newCoord) == '|' -> UP
            direction == DOWN && tileAt(newCoord) == '|' -> DOWN
            direction == DOWN && tileAt(newCoord) == 'L' -> RIGHT
            direction == LEFT && tileAt(newCoord) == 'L' -> UP
            direction == DOWN && tileAt(newCoord) == 'J' -> LEFT
            direction == RIGHT && tileAt(newCoord) == 'J' -> UP
            direction == RIGHT && tileAt(newCoord) == '7' -> DOWN
            direction == UP && tileAt(newCoord) == '7' -> LEFT
            else -> error("Unkown turn $fromCoord to $newCoord")
        }
        return newDirection to newCoord
    }

    private fun startTile(dir1: Direction, dir2: Direction): Char =
        when {
            (dir1 == UP && dir2 == DOWN) || (dir1 == DOWN && dir2 == UP) -> '|'
            (dir1 == LEFT && dir2 == RIGHT) || (dir1 == RIGHT && dir2 == LEFT) -> '-'
            (dir1 == UP && dir2 == RIGHT) || (dir1 == RIGHT && dir2 == UP) -> 'L'
            (dir1 == UP && dir2 == LEFT) || (dir1 == LEFT && dir2 == UP) -> 'J'
            (dir1 == DOWN && dir2 == LEFT) || (dir1 == LEFT && dir2 == DOWN) -> '7'
            else -> 'F'
        }

    data class MainLoop(val start: Coord, val startTileSymbol: Char, val stepsToEnd: Int, val path: Set<Coord>) {
        fun contains(coord: Coord) = coord == start || path.contains(coord)
    }

    private fun findPath(start: Coord): MainLoop {
        val directions = moveableDirections(start)
        val path1 = mutableListOf(move(directions[0] to start))
        val path2 = mutableListOf(move(directions[1] to start))
        val startTileSymbol = startTile(directions[0], directions[1])

        do {
            path1.add(move(path1.last()))
            path2.add(move(path2.last()))
        } while (path1.last().second != path2.last().second)

        val coords = path1.map { it.second } + path2.map { it.second }
        return MainLoop(start, startTileSymbol, path1.size, coords.toSet())
    }

    private fun countPipes(fromCoord: Coord, mainLoop: MainLoop) =
        (0..fromCoord.x).fold(0) { acc, x ->
            val coord = Coord(x, fromCoord.y)
            val tile = when {
                coord == mainLoop.start -> mainLoop.startTileSymbol
                else -> tileAt(coord)
            }

            acc + if (mainLoop.contains(coord) && tile in "L|J") 1 else 0
        }

    private fun isEnclosed(coord: Coord, mainLoop: MainLoop): Boolean {
        val isInside = countPipes(coord, mainLoop) % 2 != 0
        val isGround = tileAt(coord) == '.'
        val isJunk = !mainLoop.contains(coord)

        return isInside && (isGround || isJunk)
    }

    fun part1(): Int = findPath(start).stepsToEnd

    fun part2(): Int {
        val mainLoop = findPath(start)
        return coords.count { isEnclosed(it, mainLoop) }
    }

}