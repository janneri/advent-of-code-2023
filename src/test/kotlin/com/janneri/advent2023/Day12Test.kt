
import com.janneri.advent2023.Day12
import com.janneri.advent2023.util.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {
    @Test
    fun part1_test_groupSizes() {
        val day = Day12(readInput("Day12_test"))
        assertEquals(listOf(1,1,3), day.groupSizes(".#...#....###."))
        assertEquals(listOf(1,3,1,6), day.groupSizes(".#.###.#.######"))
        assertEquals(listOf(1,6,5), day.groupSizes("#....######..#####."))
    }

    @Test
    fun part1_test_place() {
        val day = Day12(readInput("Day12_test"))
        assertEquals("#.#.###", day.replaceQuestionMarks("???.###", "101"))
        assertEquals(".##...#...###.", day.replaceQuestionMarks(".??..??...?##.", "11011"))
    }

    @Test
    fun part1_test() {
        val result = Day12(readInput("Day12_test")).part1()
        assertEquals(21, result)
    }

    @Test
    fun part1_real() {
        val result = Day12(readInput("Day12")).part1()
        assertEquals(7017, result)
    }
    
    @Test
    fun part2_test() {
        val result = Day12(readInput("Day12_test")).part2()
        assertEquals(525152, result)
    }

    @Test
    fun part2_real() {
        val result = Day12(readInput("Day12")).part2()
        assertEquals(527570479489, result)
    }
}