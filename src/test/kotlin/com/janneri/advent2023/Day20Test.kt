import com.janneri.advent2023.Day20
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day20Test {
    @Test
    fun part1_test() {
        val result = Day20(readInput("Day20_test")).part1()
        assertEquals(32000000, result)
    }

    @Test
    fun part1_test2() {
        val result = Day20(readInput("Day20_test2")).part1()
        assertEquals(11687500, result)
    }

    @Test
    fun part1_real() {
        val result = Day20(readInput("Day20")).part1()
        assertEquals(777666211, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day20(readInput("Day20_test")).part2()
        assertEquals(2, result)
    }

    @Test
    fun part2_real() {
        val result = Day20(readInput("Day20")).part2()
        assertEquals(2, result)
    }
}