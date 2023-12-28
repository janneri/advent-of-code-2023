// See puzzle in https://adventofcode.com/2023/day/24
package com.janneri.advent2023

import com.janneri.advent2023.util.Coord3d
import com.janneri.advent2023.util.almostEqual
import com.janneri.advent2023.util.parseLongs
import com.janneri.advent2023.util.permutations

class Day24(val inputLines: List<String>) {
    private val hailStones = inputLines.map { Hailstone.parse(it) }

    data class Hailstone(val pos: Coord3d, val velocity: Coord3d) {
        // Transfer to general form y=ax+c
        private val slope: Double = velocity.y.toDouble() / velocity.x.toDouble()
        // c=y-ax
        private val c = pos.y - slope * pos.x
        // Note. Sometimes line is represented as ax + by + c = 0
        // The general form can be converted: y = ax + c => ax - y + c = 0 (where b = -1)

        private fun isAFutureXPos(x: Double) =
            (pos.x <= x && velocity.x > 0) || (x <= pos.x && velocity.x < 0)

        fun willCollideXY(other: Hailstone, testArea: LongRange): Boolean {
            // parallel planes never collide
            if (slope.almostEqual(other.slope)) return false

            val (x, y) = intersectionXY(other)
            val (min, max) = testArea.first.toDouble() to testArea.last.toDouble()
            val inArea = x in min..max && y in min..max

            return inArea && isAFutureXPos(x) && other.isAFutureXPos(x)
        }

        private fun positionAfter(time: Double) =
            Triple(pos.x + velocity.x * time,
                pos.y + velocity.y * time,
                pos.z + velocity.z * time)

        fun willCollideWith(other: Hailstone): Boolean {
            // x = 0, v = 2 and otherX = 2, otherV = 1
            // The netspeed is v - otherV
            // The initial distance is otherX - x
            // Example (x=0, v=2), (x=2, v=1) => t = (2-0)/(2-1) = 2
            // Example (x=0, v=1), (x=2, v=2) => t = (2-0)/(1-2) = -2 (stone cannot catch the other stone)
            val t = when {
                velocity.x != other.velocity.x -> (other.pos.x - pos.x).toDouble() / (velocity.x - other.velocity.x)
                velocity.y != other.velocity.y -> (other.pos.y - pos.y).toDouble() / (velocity.y - other.velocity.y)
                velocity.z != other.velocity.z -> (other.pos.z - pos.z).toDouble() / (velocity.z - other.velocity.z)
                else -> return false
            }

            return if (t < 0) return false else positionAfter(t) == other.positionAfter(t)
        }

        fun intersectionXY(other: Hailstone): Pair<Double, Double> {
            // y1 = ax + c
            // y2 = bx + d
            // ax + c = bx + d
            // x = d - c / a - b
            val x = (other.c - c) / (this.slope - other.slope)
            val y = slope * x + c
            return x to y
        }

        companion object {
            fun parse(str: String): Hailstone =
                str.split("@").let { (coordStr, velocityStr) ->
                    val coordParts = parseLongs(coordStr)
                    val velocityParts = parseLongs(velocityStr)
                    return Hailstone(
                        Coord3d(coordParts[0], coordParts[1], coordParts[2]),
                        Coord3d(velocityParts[0], velocityParts[1], velocityParts[2])
                    )
                }
        }
    }

    fun part1(testArea: LongRange): Int {
        return hailStones.permutations().count { (h1, h2) ->
            h1.willCollideXY(h2, testArea)
        }
    }

    private fun findRockVelocities(hailstones: List<Hailstone>, limit: LongRange): List<Coord3d> {
        val invalidXVelocities = mutableSetOf<LongRange>()
        val invalidYVelocities = mutableSetOf<LongRange>()
        val invalidZVelocities = mutableSetOf<LongRange>()

        fun MutableSet<LongRange>.addImpossible(p0: Long, v0: Long, p1: Long, v1: Long) {
            // p1 can never catch p0, because p0 is moving away faster than p1
            if (p0 > p1 && v0 > v1) add(v1..v0)
            // p0 can never catch p1, because p1 is moving away faster than p0
            if (p1 > p0 && v1 > v0) add(v0..v1)
        }

        hailstones.permutations().forEach { (h1, h2) ->
            invalidXVelocities.addImpossible(h1.pos.x, h1.velocity.x, h2.pos.x, h2.velocity.x)
            invalidYVelocities.addImpossible(h1.pos.y, h1.velocity.y, h2.pos.y, h2.velocity.y)
            invalidZVelocities.addImpossible(h1.pos.z, h1.velocity.z, h2.pos.z, h2.velocity.z)
        }

        val possibleRockX = limit.filter { x -> invalidXVelocities.none { x in it } }
        val possibleRockY = limit.filter { y -> invalidYVelocities.none { y in it } }
        val possibleRockZ = limit.filter { z -> invalidZVelocities.none { z in it } }

        return buildList {
            for (x in possibleRockX) {
                for (y in possibleRockY) {
                    for (z in possibleRockZ) {
                        add(Coord3d(x, y, z))
                    }
                }
            }
        }
    }

    private fun findRockPosition(h1: Hailstone, h2: Hailstone, rockVelocity: Coord3d): Hailstone? {
        // When we substract the rock velocity from hailstone velocities,
        // the rock becomes stationary, and the hailstones intersect at rock position
        val hv1 = h1.copy(velocity = h1.velocity.minus(rockVelocity))
        val hv2 = h2.copy(velocity = h2.velocity.minus(rockVelocity))

        val (x, y) = hv1.intersectionXY(hv2)
        // newX = x + time * velocityX
        // time = (newX - x) / velocityX
        val time = (x - hv1.pos.x) / hv1.velocity.x

        if (time < 0) return null

        return Hailstone(
            Coord3d(x.toLong(), y.toLong(), h1.pos.z + (hv1.velocity.z * time).toLong()),
            rockVelocity
        )
    }

    fun part2(): Long {
        val (h1, h2) = hailStones
        val limit = if (hailStones.size > 5) LongRange(-250, 250) else LongRange(-5, 5)

        return findRockVelocities(hailStones, limit)
            .also { println(it) }
            .mapNotNull { velocity -> findRockPosition(h1, h2, velocity) }
            .first { rock -> hailStones.all { it.willCollideWith(rock) } }
            .let { it.pos.x + it.pos.y + it.pos.z }
    }

}