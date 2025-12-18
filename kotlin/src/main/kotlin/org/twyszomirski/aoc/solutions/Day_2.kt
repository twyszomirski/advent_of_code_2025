package org.twyszomirski.aoc.solutions

import java.io.File

class Day_2 {

    fun solve() {
        val lines = File("src/main/resources/input_day_2.txt").readLines()
        println("======== Day 2 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val ranges = input.first().split(",").map { it.split("-") }.map {
            Pair(it[0].toLong(), it[1].toLong())
        }
        val count = ranges.map { countMirrorNumbers(it) }.flatten().sumOf { it }
        println(count)
    }

    private fun countMirrorNumbers(range: Pair<Long, Long>): List<Long> {
        return (range.first..range.second).filter {
            val asString = it.toString()
            if (asString.length % 2 == 0) {
                asString.substring(0, asString.length / 2) == asString.substring(asString.length / 2)
            } else {
                false
            }
        }
    }

    fun part2(input: List<String>) {
        val ranges = input.first().split(",").map { it.split("-") }.map {
            Pair(it[0].toLong(), it[1].toLong())
        }

        val v = hasRepetitions("2121212121")
        val a = hasRepetitions("38593859")
        val b = hasRepetitions("666622666622")
        val repetitions = ranges.map { repetitions(it) }
        val sum = repetitions.flatten().sumOf { it }
        println(sum)
    }

    private fun repetitions(range: Pair<Long, Long>): List<Long> {
        return (range.first..range.second).filter {
            hasRepetitions(it.toString())
        }
    }

    private fun hasRepetitions(number: String): Boolean {
        val frequencies = frequencyMap(number).values.sorted()
        if (number.length > 1 && frequencies.size == 1) {
            return true
        }
        val repetitions = gdc(frequencies.first(), frequencies.last())
        if (repetitions > 1) {
            val firstCheck = frequencies.all { it % repetitions == 0 }
            if (firstCheck) {
                val windowSize = number.length / repetitions
                return number.windowed(windowSize, windowSize).toSet().size == 1
            } else {
                return false
            }
        }
        return false
    }

    fun gdc(a: Int, b: Int): Int {
        var num1 = a
        var num2 = b
        while (num2 != 0) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        return num1
    }

    private fun frequencyMap(number: String): Map<String, Int> {
        return number.split("").filter { it.isNotBlank() }.groupingBy { it }.eachCount();
    }


}