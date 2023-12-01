package com.janneri.advent2023
// See puzzle in https://adventofcode.com/2020/day/2

class Day02of2020(inputLines: List<String>) {

    data class Password(val range: IntRange, val letter: Char, val password: String) {
        companion object {
            private val pattern = """^(\d+)-(\d+) (\w): (.+)$""".toRegex()

            fun of(input: String): Password {
                val (min, max, letter, password) = pattern.find(input)!!.destructured
                return Password(min.toInt() .. max.toInt(), letter.first(), password)
            }
        }
    }

    private val passwordList = inputLines.map { Password.of(it) }

    private fun countPasswords(predicate: (Password) -> Boolean) = passwordList.count { predicate(it) }

    fun part1(): Int =
        countPasswords { pwd -> pwd.range.contains(pwd.password.count { it == pwd.letter }) }

    fun part2(): Int =
        countPasswords { pwd ->
            (pwd.password[pwd.range.first - 1] == pwd.letter) xor (pwd.password[pwd.range.last - 1] == pwd.letter)
        }
}