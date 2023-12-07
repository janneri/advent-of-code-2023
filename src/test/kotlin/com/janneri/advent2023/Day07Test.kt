import com.janneri.advent2023.Day07
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day07Test {
    @Test
    fun part1_test() {
        val result = Day07(readInput("Day07_test")).part1()
        assertEquals(6440, result)
    }

    @Test
    fun part1_real() {
        val result = Day07(readInput("Day07")).part1()
        assertEquals(245794640, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day07(readInput("Day07_test")).part2()
        assertEquals(5905, result)
    }

    @Test
    fun part2_real() {
        val result = Day07(readInput("Day07")).part2()
        assertEquals(247899149, result)
    }
}