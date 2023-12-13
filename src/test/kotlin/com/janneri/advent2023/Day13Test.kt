import com.janneri.advent2023.Day13
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test {
    @Test
    fun part1_test() {
        val result = Day13(readInput("Day13_test")).part1()
        assertEquals(405, result)
    }

    @Test
    fun part1_real() {
        val result = Day13(readInput("Day13")).part1()
        assertEquals(37381, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day13(readInput("Day13_test")).part2()
        assertEquals(400, result)
    }

    @Test
    fun part2_real() {
        val result = Day13(readInput("Day13")).part2()
        assertEquals(28210, result)
    }
}