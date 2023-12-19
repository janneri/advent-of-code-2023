import com.janneri.advent2023.Day17
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    fun part1_test() {
        val result = Day17(readInput("Day17_test")).part1()
        assertEquals(102, result)
    }

    @Test
    fun part1_real() {
        val result = Day17(readInput("Day17")).part1()
        assertEquals(845, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day17(readInput("Day17_test")).part2()
        assertEquals(94, result)
    }

    @Test
    fun part2_test2() {
        val result = Day17(readInput("Day17_test2")).part2()
        assertEquals(71, result)
    }

    @Test
    fun part2_real() {
        val result = Day17(readInput("Day17")).part2()
        assertEquals(993, result)
    }
}