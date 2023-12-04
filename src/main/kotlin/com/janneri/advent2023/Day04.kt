// See puzzle in https://adventofcode.com/2023/day/4
package com.janneri.advent2023

class Day04(inputLines: List<String>) {
    data class Card(val cardNum: Int, val winningCount: Int, val newCardNums: IntRange, var newCards: Set<Card>) {
        companion object {
            private fun parseNums(str: String): Set<Int> =
                str.trim().replace("  ", " ").split(" ").map { it.trim().toInt() }.toSet()

            fun of(str: String): Card {
                val (cardNumStr, numbers) = str.split(":")
                val (winning, all) = numbers.split("|")
                val cardNum = cardNumStr.substringAfter(" ").trim().toInt()
                val winningNums = parseNums(winning).filter { parseNums(all).contains(it) }
                val yieldNums = IntRange(cardNum + 1, cardNum + winningNums.size)
                return Card(
                    cardNumStr.substringAfter(" ").trim().toInt(),
                    winningNums.size,
                    yieldNums,
                    emptySet()
                )
            }
        }

        fun points(): Int {
            return when {
                winningCount >= 1 -> IntRange(1, winningCount).reduce {acc, _ -> acc * 2 }
                else -> 0
            }
        }
    }

    data class Deck(val cards: List<Card>) {
        private val originalCardsByNum: Map<Int, Card> =
            cards.fold(mutableMapOf()) { acc, card -> acc[card.cardNum] = card; acc}

        init {
            cards.forEach { card -> card.newCards = card.newCardNums.map { originalCardsByNum[it]!! }.toSet() }
        }

        val currentCards: Map<Int, MutableList<Card>> =
            cards.fold(mutableMapOf()) { acc, card ->
                acc.putIfAbsent(card.cardNum, mutableListOf())
                acc[card.cardNum]!!.add(card)
                acc
            }

        var currentCardNum = 1
        var currentIndex = 0
        var gameOver = false

        fun playCard() {
            val currentCard = currentCards[currentCardNum]!![currentIndex]

            if (currentCard.newCards.isEmpty() && currentCardNum == cards.size) {
                gameOver = true
            }
            else {
                currentCard.newCards.forEach { newCard -> currentCards[newCard.cardNum]!!.add(newCard) }
                if (currentIndex == currentCards[currentCard.cardNum]!!.lastIndex) {
                    currentIndex = 0
                    currentCardNum += 1
                }
                else {
                    currentIndex += 1
                }
            }
        }
    }

    private val deck = Deck(inputLines.map { Card.of(it) })

    fun part1(): Int = deck.cards.sumOf { it.points() }

    fun part2(): Int {
        do {
            deck.playCard()
        } while (!deck.gameOver)

        return deck.currentCards.map { it.value.size }.sum()
    }
}