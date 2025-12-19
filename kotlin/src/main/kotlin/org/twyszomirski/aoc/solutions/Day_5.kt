package org.twyszomirski.aoc.solutions

import java.io.File
import java.util.Stack

class Day_5 {

    fun solve() {
        val lines = File("src/main/resources/input_day_5.txt").readLines()
        println("======== Day 5 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val (r, i) = input.partition { it.contains("-") }
        val ranges = r.map { it.split("-") }.map { it[0].toLong() to it[1].toLong() }
        val indexes = i.filter { it.isNotBlank() }.map { it.toLong() }

        val count = indexes.count { idx -> ranges.any { it.isInRange(idx) } }
        println(count)
    }

    private fun Pair<Long, Long>.isInRange(num: Long): Boolean {
        return this.first <= num && this.second >= num
    }

    fun part2(input: List<String>) {
        val ranges = input.filter { it.contains("-") }.map { it.split("-") }.map { it[0].toLong() to it[1].toLong() }
        val mergedRanges = mergeRanges(ranges)
        val sum = mergedRanges.sumOf { it.second - it.first + 1 }
        println(sum)
    }

    private fun mergeRanges(ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
        val sortedRanges = ranges.sortedBy { it.first }
        val result = Stack<Pair<Long, Long>>()
        result.push(sortedRanges.first())
        sortedRanges.drop(1).forEach { range ->
            val prev = result.pop()
            if (range.first >= prev.first && range.first <= prev.second) {
                result.push(prev.first to Math.max(prev.second, range.second))
            } else {
                result.push(prev)
                result.push(range)
            }
        }
        return result
    }


}

