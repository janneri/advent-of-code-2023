import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration


object AdventOfCodeTemplateUtil {
    private fun createDay(dayNum: Int) {
        val dir = Path.of("src")
        val dayPrefix = String.format("Day%02d", dayNum)
        val inputTxtFile = dir.resolve("$dayPrefix.txt").toFile()
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

        Files.writeString(inputTxtFile.toPath(), downloadInput(dayNum))
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

    private fun getHttpClient(): HttpClient {
        CookieHandler.setDefault(CookieManager())
        val cookieManager = CookieHandler.getDefault() as CookieManager
        val sessionCookieValue = File(System.getProperty("user.home"),".adventofcode.session").readText().trim()
        val aocSessionCookie = HttpCookie("session", sessionCookieValue).apply {
            path = "/"
            version = 0
        }
        cookieManager.cookieStore.add(URI("https://adventofcode.com"), aocSessionCookie)
        return HttpClient.newBuilder()
            .cookieHandler(cookieManager)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
    }

    private fun downloadInput(dayNum: Int): String? {
        val client = getHttpClient()
        val req = HttpRequest.newBuilder()
            .uri(URI.create("https://adventofcode.com/2022/day/$dayNum/input"))
            .GET().build()

        return client.send(req, HttpResponse.BodyHandlers.ofString()).body().trim()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        createDay(2)
    }
}