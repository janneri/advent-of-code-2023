import java.nio.file.Files
import java.nio.file.Path

object AdventOfCodeTemplateUtil {
    private fun createDay(dayNum: Int) {
        val dir = Path.of("src")
        val dayPrefix = String.format("Day%02d", dayNum)
        dir.resolve("$dayPrefix.txt").toFile().createNewFile()
        dir.resolve("${dayPrefix}_test.txt").toFile().createNewFile()
        val mainFile = dir.resolve("${dayPrefix}.kt").toFile()
        Files.writeString(mainFile.toPath(), """
        // See puzzle in https://adventofcode.com/2023/day/$dayNum
        
        fun part1(inputLines: List<String>): Int {
            return 0
        }
        
        fun part2(inputLines: List<String>): Int {
            return 0
        }
        
        fun main() {
            val testInput = readInput("${dayPrefix}_test")
            check(part1(testInput) == 1)
            
            val input = readInput("$dayPrefix")
            part1(input).println()
            part2(input).println()
        }
        """.trimIndent())
    }

    fun equals(expected: Number, actual: Number) {
        check (expected == actual) { "Expected $expected, but was $actual" }
    }

    fun equals(expected: String, actual: String) {
        check (expected == actual) { "Expected $expected, but was $actual" }
    }

    fun debug(str: String, level: Int) {
        println("${"  ".repeat(level)} $str")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        createDay(2)
    }
}