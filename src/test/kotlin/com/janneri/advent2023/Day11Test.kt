import com.janneri.advent2023.Day11
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun part1_test() {
        val result = Day11(readInput("Day11_test")).part1()
        assertEquals(374, result)
    }

    @Test
    fun part1_test2() {
        val result = Day11(readInput("Day11_test2")).part1()
        assertEquals(5, result)
    }

    @Test
    fun part1_real() {
        val result = Day11(readInput("Day11")).part1()
        assertEquals(9795148, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day11(readInput("Day11_test"), 10 - 1).part2()
        assertEquals(1030, result)
    }

    @Test
    fun part2_test2() {
        val result = Day11(readInput("Day11_test"), 100 - 1).part2()
        assertEquals(8410, result)
    }

    @Test
    fun part2_real() {
        val result = Day11(readInput("Day11"), 1000000 - 1).part2()
        assertEquals(650672493820L, result)
    }
}