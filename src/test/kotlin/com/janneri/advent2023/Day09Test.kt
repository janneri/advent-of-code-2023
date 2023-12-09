import com.janneri.advent2023.Day09
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {
    @Test
    fun part1_test() {
        val result = Day09(readInput("Day09_test")).part1()
        assertEquals(114, result)
    }

    @Test
    fun part1_real() {
        val result = Day09(readInput("Day09")).part1()
        assertEquals(1904165718, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day09(readInput("Day09_test")).part2()
        assertEquals(2, result)
    }

    @Test
    fun part2_real() {
        val result = Day09(readInput("Day09")).part2()
        assertEquals(964, result)
    }
}