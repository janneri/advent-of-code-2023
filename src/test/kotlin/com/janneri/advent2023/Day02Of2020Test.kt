package com.janneri.advent2023

import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Of2020Test {
    @Test
    fun part1_test() {
        val result = Day02of2020(readInput("Day02_test")).part1()
        assertEquals(2, result)
    }

    @Test
    fun part1_real() {
        val result = Day02of2020(readInput("Day02")).part1()
        assertEquals(500, result)
    }

    @Test
    fun part2_test() {
        val result = Day02of2020(readInput("Day02_test")).part2()
        assertEquals(1, result)
    }

    @Test
    fun part2_real() {
        val result = Day02of2020(readInput("Day02")).part2()
        assertEquals(313, result)
    }
}