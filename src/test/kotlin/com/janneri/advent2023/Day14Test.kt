import com.janneri.advent2023.Day14
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test {
    @Test
    fun part1_test() {
        val result = Day14(readInput("Day14_test")).part1()
        assertEquals(136, result)
    }

    @Test
    fun part1_real() {
        val result = Day14(readInput("Day14")).part1()
        assertEquals(113525, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day14(readInput("Day14_test")).part2()
        assertEquals(64, result)
    }

    @Test
    fun part2_real() {
        val result = Day14(readInput("Day14")).part2()
        assertEquals(101292, result)
    }
}