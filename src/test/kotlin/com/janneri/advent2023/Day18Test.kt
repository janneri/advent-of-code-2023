import com.janneri.advent2023.Day18
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day18Test {
    @Test
    fun part1_test() {
        val result = Day18(readInput("Day18_test")).part1()
        assertEquals(62, result)
    }

    @Test
    fun part1_real() {
        val result = Day18(readInput("Day18")).part1()
        assertEquals(62365, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day18(readInput("Day18_test")).part2()
        assertEquals(952408144115, result)
    }

    @Test
    fun part2_real() {
        val result = Day18(readInput("Day18")).part2()
        assertEquals(159485361249806, result)
    }
}