import com.janneri.advent2023.Day04
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {
    @Test
    fun part1_test() {
        val result = Day04(readInput("Day04_test")).part1()
        assertEquals(13, result)
    }

    @Test
    fun part1_real() {
        val result = Day04(readInput("Day04")).part1()
        assertEquals(25183, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day04(readInput("Day04_test")).part2()
        assertEquals(30, result)
    }

    @Test
    fun part2_real() {
        val result = Day04(readInput("Day04")).part2()
        assertEquals(5667240, result)
    }
}