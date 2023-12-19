import com.janneri.advent2023.Day19
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day19Test {
    @Test
    fun part1_test() {
        val result = Day19(readInput("Day19_test")).part1()
        assertEquals(19114, result)
    }

    @Test
    fun part1_real() {
        val result = Day19(readInput("Day19")).part1()
        assertEquals(509597, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day19(readInput("Day19_test")).part2()
        assertEquals(167409079868000, result)
    }

    @Test
    fun part2_real() {
        val result = Day19(readInput("Day19")).part2()
        assertEquals(143219569011526, result)
    }
}