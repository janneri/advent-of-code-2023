import com.janneri.advent2023.Day08
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {
    @Test
    fun part1_test() {
        val result = Day08(readInput("Day08_test")).part1()
        assertEquals(6, result)
    }

    @Test
    fun part1_real() {
        val result = Day08(readInput("Day08")).part1()
        assertEquals(15517, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day08(readInput("Day08_test2")).part2()
        assertEquals((6).toBigInteger(), result)
    }

    @Test
    fun part2_real() {
        val result = Day08(readInput("Day08")).part2()
        assertEquals((14935034899483).toBigInteger(), result)
    }
}