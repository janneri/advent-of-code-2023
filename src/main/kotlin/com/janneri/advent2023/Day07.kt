// See puzzle in https://adventofcode.com/2023/day/7
package com.janneri.advent2023


class Day07(inputLines: List<String>, private var useJoker: Boolean = false) {
    private fun cardRank(char: Char) = when {
        useJoker -> listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed().indexOf(char)
        else -> listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed().indexOf(char)
    }

    data class Hand(val cards: CharArray, val bid: Int) {
        lateinit var handType: HandType
        fun replaceJokers(withCard: Char) =
            Hand(this.cards.map { if (it == 'J') withCard else it }.toCharArray(), this.bid)
    }

    fun Hand.compareTo(other: Hand): Int {
        val handTypeCompare = this.handType.rank.compareTo(other.handType.rank)
        if (handTypeCompare == 0) {
            return this.cards.foldIndexed(0) { index, acc, card ->
                when {
                    acc == 0 -> cardRank(card).compareTo(cardRank(other.cards[index]))
                    else -> acc
                }
            }
        }
        return handTypeCompare
    }

    sealed class HandType(val rank: Int)
    data class FiveOfAKind(val char: Char): HandType(7)
    data class FourOfAKind(val char: Char): HandType(6)
    data class FullHouse(val char1: Char, val char2: Char): HandType(5)
    data class ThreeOfAKind(val char: Char): HandType(4)
    data class TwoPair(val char1: Char, val char2: Char): HandType(3)
    data class OnePair(val char: Char): HandType(2)
    data class HighCard(val char: Char): HandType(1)

    private val hands: List<Hand> = inputLines
        .map { str -> str.split(" ")
            .let { Hand(it[0].toCharArray(), it[1].toInt()) } }

    // Not needed for the puzzle, but nice to know for the HighCard hand
    private fun bestCard(cards: CharArray): Char =
        cards.fold(cards.first() to cardRank(cards.first())) { acc, card ->
            val cardRank = cardRank(card)
            if (cardRank > acc.second) card to cardRank else acc
        }.first

    // Returns the cards that are found "amount" times in the hand
    // For example: sameCards("AA122", 2) would return setOf('A', '2')
    private fun sameCards(hand: Hand, amount: Int): Set<Char> {
        var cards = hand.cards
        var jokerCount = 0
        if (useJoker) {
            jokerCount = hand.cards.count { it == 'J' }
            if (jokerCount == 5 && amount == 5) return setOf('J')
            cards = hand.cards.filter { it != 'J' }.toCharArray()
        }

        return cards.groupBy { it }.filter { jokerCount + it.value.size >= amount }.keys
    }


    private fun hasSameCards(hand: Hand, amount: Int) = sameCards(hand, amount).isNotEmpty()

    private fun findFullHouse(theHand: Hand): FullHouse? {
        val hand = when {
            useJoker -> theHand.replaceJokers(theHand.cards.first { it != 'J' })
            else -> theHand
        }

        val three = sameCards(hand, 3)
        if (three.isEmpty()) return null
        val two = sameCards(hand, 2).filter { it != three.first() }
        if (two.isEmpty()) return null
        return FullHouse(three.first(), two.first())
    }

    private fun findTwoPair(hand: Hand): TwoPair? {
        val pairs = sameCards(hand, 2)
        if (pairs.size != 2) return null
        return TwoPair(pairs.first(), pairs.last())
    }

    private fun detectHandType(hand: Hand): HandType =
        when {
            hasSameCards(hand, 5) -> FiveOfAKind(sameCards(hand, 5).first())
            hasSameCards(hand, 4) -> FourOfAKind(sameCards(hand, 4).first())
            findFullHouse(hand) != null -> findFullHouse(hand)!!
            hasSameCards(hand, 3) -> ThreeOfAKind(sameCards(hand, 3).first())
            findTwoPair(hand) != null -> findTwoPair(hand)!!
            hasSameCards(hand, 2) -> OnePair(sameCards(hand, 2).first())
            else -> HighCard(bestCard(hand.cards))
        }

    private fun solve(withJokers: Boolean): Int {
        this.useJoker = withJokers
        hands.forEach { it.handType = detectHandType(it) }
        return hands.sortedWith { h1, h2 -> h1.compareTo(h2) }
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    fun part1(): Int = solve(false)
    
    fun part2(): Int = solve(true)
}