import com.janneri.advent2023.Day03
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {
    @Test
    fun part1_test() {
        val result = Day03(readInput("Day03_test")).part1()
        assertEquals(4361, result)
    }

    @Test
    fun part1_real() {
        val result = Day03(readInput("Day03")).part1()
        assertEquals(559667, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day03(readInput("Day03_test")).part2()
        assertEquals(467835, result)
    }

    @Test
    fun part2_real() {
        val result = Day03(readInput("Day03")).part2()
        assertEquals(86841457, result)
    }
}