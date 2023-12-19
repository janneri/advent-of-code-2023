// See puzzle in https://adventofcode.com/2023/day/19
package com.janneri.advent2023

import com.janneri.advent2023.util.intersectRange

private typealias Part = Map<Char, Int>

class Day19(val inputLines: List<String>) {
    sealed class Rule(open val nextWorkflow: String)
    data class OtherWiseRule(override val nextWorkflow: String) : Rule(nextWorkflow)
    interface ConditionRule {
        val nextWorkflow: String
        val category: Char
        fun matchingRange(): IntRange
        fun unmatchingRange(): IntRange
    }
    data class LessThanRule(override val category: Char, val amount: Int, override val nextWorkflow: String): Rule(nextWorkflow), ConditionRule {
        override fun matchingRange(): IntRange = 1..<amount
        override fun unmatchingRange(): IntRange = amount..4000
    }
    data class MoreThanRule(override val category: Char, val amount: Int, override val nextWorkflow: String) : Rule(nextWorkflow), ConditionRule {
        override fun matchingRange(): IntRange = amount + 1..4000
        override fun unmatchingRange(): IntRange = 1..amount
    }
    data class Workflow(val name: String, val rules: List<Rule>)

    private val inputSections = inputLines.joinToString("\n").split("\n\n")
    private val workflows = parseWorkflows(inputSections[0].split("\n")).associateBy { it.name }
    private val parts = parseParts(inputSections[1].split("\n"))

    private fun parseWorkflows(lines: List<String>) =
        lines.map { line ->
            val name  = line.takeWhile { it != '{' }
            val ruleString = line.dropWhile { it != '{' }
            val parts = ruleString.drop(1).dropLast(1).split(",")
            Workflow(name, parts.map { parseRule(it) })
        }

    private fun parseRule(str: String): Rule {
        if (!str.contains(":")) return OtherWiseRule(str)
        val nextWorkflow = str.split(":")[1]
        val ruleStr = str.takeWhile { it != ':' }

        return if (ruleStr.contains("<")) LessThanRule(ruleStr.first(), ruleStr.split("<")[1].toInt(), nextWorkflow)
        else MoreThanRule(ruleStr.first(), ruleStr.split(">")[1].toInt(), nextWorkflow)
    }

    private fun parseParts(lines: List<String>): List<Part> {
        val pattern = """^\{x=(\d+),m=(\d+),a=(\d+),s=(\d+)\}$""".toRegex()
        return lines.map { line ->
            val (x, m, a, s) = pattern.find(line)!!.destructured
            mapOf('x' to x.toInt(), 'm' to m.toInt(), 'a' to a.toInt(), 's' to s.toInt())
        }
    }

    private fun findNextWorkFlowName(fromWorkFlow: String, part: Part): String =
        workflows.getValue(fromWorkFlow).rules.find { rule ->
            when (rule) {
                is LessThanRule -> part[rule.category]!! < rule.amount
                is MoreThanRule -> part[rule.category]!! > rule.amount
                is OtherWiseRule -> true
            }
        }!!.nextWorkflow

    private val endStates = setOf("A", "R")

    fun part1(): Int {
        val currentWorkFlowNames = parts.map { "in" }.toTypedArray()
        do {
            parts
                .forEachIndexed { index, part ->
                    if (currentWorkFlowNames[index] !in endStates) {
                        currentWorkFlowNames[index] = findNextWorkFlowName(currentWorkFlowNames[index], part)
                    }
                }
        } while (currentWorkFlowNames.any { !endStates.contains(it) })

        return parts
            .filterIndexed {index, _ -> currentWorkFlowNames[index] == "A"}
            .sumOf { it.values.sum() }
    }

    private fun combinations(workflowName: String, ranges: Map<Char, IntRange>): Long {
        return when (workflowName) {
            "R"  -> 0
            "A"  -> ranges.values.map { it.count().toLong() }.reduce(Long::times)
            else -> {
                // These ranges are mutated and passed through all rules of this workflow
                val newRanges = ranges.toMutableMap()

                workflows.getValue(workflowName).rules.sumOf { rule ->
                    when (rule) {
                        is OtherWiseRule -> combinations(rule.nextWorkflow, newRanges)
                        else -> {
                            val condRule = rule as ConditionRule

                            val currentRange = newRanges.getValue(condRule.category)
                            val matchingRange = currentRange.intersectRange(condRule.matchingRange())
                            val unmatchingRange = currentRange.intersectRange(condRule.unmatchingRange())

                            newRanges[condRule.category] = matchingRange
                            combinations(condRule.nextWorkflow, newRanges).also {
                                // We only go to the next rule, if the value does not match.
                                // So the next rule must get the unmatching range as input
                                // Example: when a rule is x<2000:foo,bar
                                // recursive call happens with x 1..1999 and the next rule gets x: 2000..4000
                                newRanges[condRule.category] = unmatchingRange
                            }
                        }
                    }
                }
            }
        }
    }

    fun part2(): Long = combinations(
            workflowName = "in",
            ranges = mapOf(
                'x' to (1..4000),
                'm' to (1..4000),
                'a' to (1..4000),
                's' to (1..4000)
            )
        )
}