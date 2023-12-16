import com.janneri.advent2023.Day16
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day16Test {
    @Test
    fun part1_test() {
        val result = Day16(readInput("Day16_test")).part1()
        assertEquals(46, result)
    }

    @Test
    fun part1_real() {
        val result = Day16(readInput("Day16")).part1()
        assertEquals(8112, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day16(readInput("Day16_test")).part2()
        assertEquals(51, result)
    }

    @Test
    fun part2_real() {
        val result = Day16(readInput("Day16")).part2()
        assertEquals(8314, result)
    }
}