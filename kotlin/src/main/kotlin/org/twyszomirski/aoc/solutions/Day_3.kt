package org.twyszomirski.aoc.solutions

import java.io.File

class Day_3 {

    fun solve() {
        val lines = File("src/main/resources/input_day_3.txt").readLines()
        println("======== Day 3 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val inputParsed = input.map { it.split("").filter { it.isNotBlank() }.map { it.toInt() } }

        val bateries = inputParsed.map {
            val maxLeft = maxLeft(it)
            val maxRight = maxRight(it, maxLeft.second)
            maxLeft.first to maxRight.first
        }

        val total = bateries.sumOf { "${it.first}${it.second}".toInt() }

        println(total)

    }

    fun maxLeft(input: List<Int>): Pair<Int, Int> {
        val max = input.subList(0, input.size - 1).max()
        return max to input.subList(0, input.size - 1).indexOf(max);
    }

    fun maxRight(input: List<Int>, leftBoundry: Int): Pair<Int, Int> {
        val max = input.subList(leftBoundry + 1, input.size).max();
        return max to input.subList(leftBoundry + 1, input.size).indexOf(max);
    }

    fun part2(input: List<String>) {

        val inputParsed = input.map { it.split("").filter { it.isNotBlank() }.map { it.toInt() } }
        val maxes = inputParsed.map { goGreedy(it, it.count() - 12) }
        val sum = maxes.sum()
        println(sum)
    }

    private fun goGreedy(digits: List<Int>, skipCount: Int): Long {
        val result = mutableListOf<Int>()
        var skipSAvailable = skipCount

        var currIdx = 0
        while (result.size < 12) {
            val (max, maxIndex) = bestFromIndex(digits, currIdx, skipSAvailable)
            result.add(max)
            val usedSkips = maxIndex - currIdx
            skipSAvailable -= usedSkips
            currIdx = maxIndex + 1
        }

        return result.joinToString("").toLong()
    }

    private fun bestFromIndex(digits: List<Int>, startIdx: Int, skipCount: Int): Pair<Int, Int> {
        var max = digits[startIdx]
        var maxIdx = startIdx
        for (i in startIdx..(startIdx + skipCount)) {
            if (digits[i] > max) {
                max = digits[i]
                maxIdx = i;
            }
        }
        return max to maxIdx
    }


}

