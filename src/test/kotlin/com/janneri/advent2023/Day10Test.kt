import com.janneri.advent2023.Day10
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {
    @Test
    fun part1_test() {
        val result = Day10(readInput("Day10_test")).part1()
        assertEquals(4, result)
    }

    @Test
    fun part1_test2() {
        val result = Day10(readInput("Day10_test2")).part1()
        assertEquals(8, result)
    }

    @Test
    fun part1_real() {
        val result = Day10(readInput("Day10")).part1()
        assertEquals(6875, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day10(readInput("Day10_test3")).part2()
        assertEquals(4, result)
    }

    @Test
    fun part2_test2() {
        val result = Day10(readInput("Day10_test4")).part2()
        assertEquals(8, result)
    }

    @Test
    fun part2_test3() {
        val result = Day10(readInput("Day10_test5")).part2()
        assertEquals(10, result)
    }

    @Test
    fun part2_real() {
        val result = Day10(readInput("Day10")).part2()
        assertEquals(471, result)
    }
}