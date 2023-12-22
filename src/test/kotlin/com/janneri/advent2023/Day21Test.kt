import com.janneri.advent2023.Day21
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun part1_test() {
        val result = Day21(readInput("Day21_test")).part1()
        assertEquals(2, result)
    }

    @Test
    fun part1_real() {
        val result = Day21(readInput("Day21")).part1()
        assertEquals(3598, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day21(readInput("Day21_test")).part2(50)
        assertEquals(2, result)
    }

    @Test
    fun part2_real() {
        val result = Day21(readInput("Day21")).part2(26501365)
        assertEquals(601441063166538, result)
    }
}