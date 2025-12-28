package org.twyszomirski.aoc.solutions

import java.io.File
import kotlin.math.max
import kotlin.math.min

class Day_10 {

    fun solve() {
        val lines = File("src/main/resources/input_day_10.txt").readLines()
        println("======== Day 10 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val machines = parseMachines(input)
        val minButtons = machines.map { machine ->
            (1..machine.buttons.size).first { permsLength ->
                val res = mutableListOf<List<Int>>()
                permutations(machine.buttonsMask(), permsLength, emptyList(), res)
                res.any { possibleButtons ->
                    possibleButtons.fold(0) { c, n -> c xor n } == machine.lightsMask()
                }
            }
        }

        println(minButtons.sum())
    }

    fun permutations(items: List<Int>, count: Int, seed: List<Int>, result: MutableList<List<Int>>) {
        if (seed.size == count) {
            result.add(seed)
            return
        }
        items.forEachIndexed { idx, item ->
            val copy = items.filterIndexed { innerIdx, _ -> innerIdx != idx }.toList()
            permutations(copy, count, seed + item, result)
        }
    }

    fun part2(input: List<String>) {

    }

    fun parseMachines(input: List<String>): List<Machine> {
        return input.map { line ->
            val l = "\\[.*\\]".toRegex().find(line)?.value!!.replace("[", "").replace("]", "")

            val b = "\\(.*\\)".toRegex().findAll(line).map {
                it.value.split(" ").map {
                    it.replace("(", "").replace(")", "").split(",").map { it.toInt() }.toList()
                }
            }.toList().first()

            val j = "\\{.*\\}".toRegex().find(line)?.value!!.replace("{", "").replace("}", "")
                .split(",").map { it.toInt() }.toList()

            Machine(l, b, j)
        }
    }

    data class Machine(val lights: String, val buttons: List<List<Int>>, val joltages: List<Int>) {
        fun lightsMask(): Int {
            val symbolsReversed = lights.toCharArray().reversed()
            var result = 0
            symbolsReversed.forEach { symbol ->
                val bitwiseSymbol = if (symbol == '#') 1 else 0
                result = (result shl 1) or bitwiseSymbol
            }
            return result
        }

        fun buttonsMask(): List<Int> {
            return buttons.map { positions ->
                var result = 0
                positions.map { pos ->
                    result = result or (1 shl pos)
                }
                result
            }
        }

    }


}