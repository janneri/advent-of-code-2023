import com.janneri.advent2023.Day25
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day25Test {
    @Test
    fun part1_test() {
        val result = Day25(readInput("Day25_test")).part1()
        assertEquals(54, result)
    }

    @Test
    fun part1_real() {
        val result = Day25(readInput("Day25")).part1()
        assertEquals(583338, result)
    }
    
}