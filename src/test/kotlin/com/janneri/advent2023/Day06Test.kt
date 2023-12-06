import com.janneri.advent2023.Day06
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {
    @Test
    fun part1_test() {
        val result = Day06(readInput("Day06_test")).part1()
        assertEquals(288, result)
    }

    @Test
    fun part1_real() {
        val result = Day06(readInput("Day06")).part1()
        assertEquals(1083852, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day06(readInput("Day06_test")).part2()
        assertEquals((71503).toULong(), result)
    }

    @Test
    fun part2_real() {
        val result = Day06(readInput("Day06")).part2()
        assertEquals((23501589).toULong(), result)
    }
}