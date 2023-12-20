// See puzzle in https://adventofcode.com/2023/day/20
package com.janneri.advent2023

import com.janneri.advent2023.util.findLCMOfListOfNumbers

class Day20(val inputLines: List<String>) {

    interface Module {
        val name: String
        val destModules: List<String>
    }

    data class OutputModule(
        override val name: String = "output",
        override val destModules: List<String> = emptyList()
    ): Module

    data class BroadcastModule(
        override val name: String,
        override val destModules: List<String>
    ) : Module {
        fun receivePulse(pulse: Pulse): Pulse {
            return Pulse(pulse.high, name, destModules)
        }
    }

    data class FlipFlopModule(
        override val name: String,
        override val destModules: List<String>,
        var on: Boolean = false
    ) : Module {
        fun receivePulse(pulse: Pulse): Pulse? {
            if (!pulse.high) {
                on = !on
                return Pulse(on, name, destModules)
            }
            return null
        }
    }

    data class ConjunctionModule(
        override val name: String,
        override val destModules: List<String>,
        var lastInputPulses: MutableMap<String, Boolean>
    ) : Module {
        private fun allHigh() = lastInputPulses.values.all { it }
        fun receivePulse(pulse: Pulse): Pulse {
            lastInputPulses[pulse.fromModule] = pulse.high
            return Pulse(!allHigh(), name, destModules)
        }
    }

    data class Pulse(val high: Boolean, val fromModule: String, val destModules: List<String>) {
        override fun toString(): String =
            "$fromModule -${if (high) "high" else "low"}-> ${destModules.joinToString(",")}"
    }

    private fun parseModule(line: String): Module {
        return line.split("->").map { it.trim() }.let { (nameStr, destStr) ->
            val destModules = destStr.split(",").map { it.trim() }
            when {
                nameStr.startsWith("%") -> FlipFlopModule(nameStr.drop(1).trim(), destModules)
                nameStr.startsWith("&") -> ConjunctionModule(nameStr.drop(1).trim(), destModules, mutableMapOf())
                else -> BroadcastModule(nameStr, destModules)
            }
        }
    }

    private fun parseModules() = inputLines.map { parseModule(it) }.associateBy { it.name }.also { moduleMap ->
        moduleMap.values.filterIsInstance<ConjunctionModule>().forEach { module ->
            val inputModules = moduleMap.values.filter { it.destModules.contains(module.name) }
            module.lastInputPulses = inputModules.associate { it.name to false }.toMutableMap()
        }
    }

    private fun newPulsesFrom(modules: Map<String, Module>, pulse: Pulse): List<Pulse> =
        pulse.destModules.mapNotNull { moduleName ->
            val module = modules[moduleName] ?: OutputModule()
            when (module) {
                is BroadcastModule -> module.receivePulse(pulse)
                is FlipFlopModule -> module.receivePulse(pulse)
                is ConjunctionModule -> module.receivePulse(pulse)
                is OutputModule -> null
                else -> error("xxx")
            }
        }

    data class ClickResult(val lowCount: Long, val highCount: Long, val pulseFound: Boolean)

    private fun clickButton(modules: Map<String, Module>, isEnd: (List<Pulse>) -> Boolean): ClickResult {
        val initialPulses = listOf(Pulse(false, "button", listOf("broadcaster")))
        var currentPulses = initialPulses

        var highCount = 0L
        var lowCount = 0L

        do {
            currentPulses = currentPulses.flatMap { pulse ->
                if (pulse.high) highCount += pulse.destModules.size
                else lowCount += pulse.destModules.size

                newPulsesFrom(modules, pulse)
            }

            if (isEnd(currentPulses)) {
                return ClickResult(lowCount, highCount, true)
            }

            val finished = currentPulses.isEmpty()
        } while (!finished)
        return ClickResult(lowCount, highCount, false)
    }

    fun part1(): Long {
        val modules = parseModules()
        var lowCount = 0L
        var highCount = 0L
        repeat(1000) {
            val clickResult = clickButton(modules) { _ -> false }
            lowCount += clickResult.lowCount
            highCount += clickResult.highCount
        }
        return lowCount * highCount
    }

    fun part2(): Long {
        // The only input of RX is ZH and ZH has four input modules
        val interestingModules = (parseModules()["zh"] as ConjunctionModule).lastInputPulses.keys

        // How many clicks are needed to make all interesting modules (sx, jt, kb, ks) to send a high pulse?
        var clickCounts = interestingModules.associateWith { 0 }.toMutableMap()

        interestingModules.forEach { moduleName ->
            val modules = parseModules()
            var clickCount = 0
            do {
                val clickResult = clickButton(modules) {
                    pulses -> pulses.any { it.high && it.fromModule == moduleName && "zh" in it.destModules }
                }
                clickCount += 1
            } while (!clickResult.pulseFound)

            clickCounts[moduleName] = clickCount
        }

        return findLCMOfListOfNumbers(clickCounts.values.map { it.toLong() })
    }

}
