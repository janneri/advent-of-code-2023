import com.janneri.advent2023.Day15
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day15Test {
    @Test
    fun part1_test() {
        val result = Day15(readInput("Day15_test")).part1()
        assertEquals(1320, result)
    }

    @Test
    fun part1_real() {
        val result = Day15(readInput("Day15")).part1()
        assertEquals(513158, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day15(readInput("Day15_test")).part2()
        assertEquals(145, result)
    }

    @Test
    fun part2_real() {
        val result = Day15(readInput("Day15")).part2()
        assertEquals(200277, result)
    }
}