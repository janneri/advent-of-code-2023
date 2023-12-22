// See puzzle in https://adventofcode.com/2023/day/22
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord3d
import com.janneri.advent2023.util.Coord3d.Companion.DOWN
import com.janneri.advent2023.util.overLaps
import com.janneri.advent2023.util.parseLongs
import kotlin.math.max
import kotlin.math.min

class Day22(val inputLines: List<String>) {

    data class Brick(val start: Coord3d, val end: Coord3d) {
        lateinit var name: String
        private val xRange = LongRange(min(start.x, end.x), max(start.x, end.x))
        private val yRange = LongRange(min(start.y, end.y), max(start.y, end.y))
        val lowest = min(start.z, end.z)
        private val highest = max(start.z, end.z)

        fun moveDown(): Brick = Brick(start.move(DOWN), end.move(DOWN)).also { it.name = this.name }

        fun isOnGround(): Boolean = lowest == 1L

        fun restsOn(otherBrick: Brick): Boolean {
            return (lowest - 1 == otherBrick.highest)
                    && xRange.overLaps(otherBrick.xRange)
                    && yRange.overLaps(otherBrick.yRange)
        }

        override fun toString(): String = name
    }

    private fun parseBricks(): List<Brick> {
        val bricks = inputLines.map { parseBrick(it) }.sortedBy { it.lowest }
        bricks.forEachIndexed { index, brick -> brick.name = (index+65).toChar().toString() }
        return bricks
    }

    private fun parseBrick(str: String) = str.split("~").let { (c1, c2) ->
        Brick(
            parseLongs(c1).let { Coord3d(it[0], it[1], it[2]) },
            parseLongs(c2).let { Coord3d(it[0], it[1], it[2]) }
        )
    }

    // Drop this brick on top of other bricks and return the new bricks and the set of bricks it lies on
    private fun drop(brick: Brick, otherBricks: List<Brick>): Pair<Brick, Set<Brick>> {
        var drops = 0
        var currentBrick = brick
        var touches: Set<Brick>
        do {
            drops += 1
            touches = otherBricks.filter { currentBrick.restsOn(it) }.toSet()
            val resting = currentBrick.isOnGround() || touches.isNotEmpty()
            if (!resting) {
                currentBrick = currentBrick.moveDown()
            }
        } while (!resting && drops < 1000)
        return currentBrick to touches
    }

    data class BrickGraph(val belowBricksByBrick: Map<Brick, Set<Brick>>,
                          val upperBricksByBrick: Map<Brick, Set<Brick>>) {
        fun bricks() = belowBricksByBrick.keys
        fun belowBricks(brick: Brick) = belowBricksByBrick.getValue(brick)

        private fun upperBricks(brick: Brick) = upperBricksByBrick.getValue(brick)

        private fun canDisintegrate(brick: Brick): Boolean {
            val upperBricks = upperBricks(brick)
            val supportedByOthers = upperBricks.all { sb -> belowBricks(sb).size > 1 }
            return upperBricks.isEmpty() || supportedByOthers
        }

        fun countBricksToDisIntegrate(): Int = bricks().count { canDisintegrate(it) }
    }

    private fun simulateDrop(bricks: List<Brick>): BrickGraph {
        val currentBricks = mutableListOf<Brick>()
        val belowBricksByBrick = mutableMapOf<Brick, Set<Brick>>()

        bricks.forEach { brick ->
            val (droppedBrick, belowBricks) = drop(brick, currentBricks)
            belowBricksByBrick[droppedBrick] = belowBricks
            currentBricks.add(droppedBrick)
        }

        val upperBricksByBrick = currentBricks.fold(mutableMapOf<Brick, Set<Brick>>()) { acc, brick ->
            val upperBricks = belowBricksByBrick.filterValues { brick in it }.map { it.key }
            acc[brick] = upperBricks.toSet()
            acc
        }
        return BrickGraph(belowBricksByBrick, upperBricksByBrick)
    }


    fun part1(): Int {
        val brickGraph = simulateDrop(parseBricks())
        return brickGraph.countBricksToDisIntegrate()
    }

    fun part2(): Int {
        val brickGraph = simulateDrop(parseBricks())
        val bricks = brickGraph.bricks()

        var total = 0
        bricks.forEachIndexed { i, brick ->
            val fallingBricks = mutableSetOf(brick)
            bricks.drop(i + 1).forEach { brick2 ->
                // Brick is falling when all the bricks below it are falling
                val belowBricks = brickGraph.belowBricks(brick2)
                if (belowBricks.isNotEmpty() && fallingBricks.containsAll(belowBricks))
                    fallingBricks.add(brick2)
            }
            total += fallingBricks.size - 1
        }

        return total
    }
}
