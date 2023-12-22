import com.janneri.advent2023.Day22
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day22Test {
    @Test
    fun part1_test() {
        val result = Day22(readInput("Day22_test")).part1()
        assertEquals(5, result)
    }

    @Test
    fun part1_real() {
        val result = Day22(readInput("Day22")).part1()
        assertEquals(490, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day22(readInput("Day22_test")).part2()
        assertEquals(7, result)
    }

    @Test
    fun part2_real() {
        val result = Day22(readInput("Day22")).part2()
        assertEquals(96356, result)
    }
}