import com.janneri.advent2023.Day24
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day24Test {
    @Test
    fun part1_test() {
        val result = Day24(readInput("Day24_test")).part1(LongRange(7, 27))
        assertEquals(2, result)
    }

    @Test
    fun part1_real() {
        val result = Day24(readInput("Day24")).part1(LongRange(200000000000000L, 400000000000000L))
        assertEquals(2, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day24(readInput("Day24_test")).part2()
        assertEquals(2, result)
    }

    @Test
    fun part2_real() {
        val result = Day24(readInput("Day24")).part2()
        assertEquals(2, result)
    }
}