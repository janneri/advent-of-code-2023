import com.janneri.advent2023.Day02
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {
    @Test
    fun part1_test() {
        val result = Day02(readInput("Day02_test")).part1()
        assertEquals(8, result)
    }

    @Test
    fun part1_real() {
        val result = Day02(readInput("Day02")).part1()
        assertEquals(2348, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day02(readInput("Day02_test")).part2()
        assertEquals(2286, result)
    }

    @Test
    fun part2_real() {
        val result = Day02(readInput("Day02")).part2()
        assertEquals(76008, result)
    }
}