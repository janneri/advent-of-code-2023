import com.janneri.advent2023.Day23
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {
    @Test
    fun part1_test() {
        val result = Day23(readInput("Day23_test")).part1()
        assertEquals(94, result)
    }

    @Test
    fun part1_real() {
        val result = Day23(readInput("Day23")).part1()
        assertEquals(2370, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day23(readInput("Day23_test")).part2()
        assertEquals(154, result)
    }

    @Test
    fun part2_real() {
        val result = Day23(readInput("Day23")).part2()
        assertEquals(6546, result)
    }
}