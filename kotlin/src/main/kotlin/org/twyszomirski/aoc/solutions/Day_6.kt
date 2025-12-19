package org.twyszomirski.aoc.solutions

import java.io.File

class Day_6 {

    fun solve() {
        val lines = File("src/main/resources/input_day_6.txt").readLines()
        println("======== Day 6 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val (o, n) = input.partition { it.contains("+") }
        val numbers = n.map { it.split(" ").filter { it.isNotBlank() }.map { it.toLong() } }
        val operations = o.map { it.split(" ").filter { it.isNotBlank() }.map { Operation.ofSymbol(it) } }.flatten()
        val flipped = flipMatrix(numbers)
        val calculated = flipped.zip(operations).map { it.first.reduce(it.second.op) }
        val sum = calculated.sumOf { it }

        println(sum)
    }


    fun part2(input: List<String>) {
        val indexes = input.first { it.contains("+") }.split("")
            .filter { !it.isEmpty() }.mapIndexed { idx, symbol -> if (symbol == " ") -1 else idx }.filter { it > 0 }

        val cheatingOnLastOne = indexes + (indexes.last() + 4)
        val justNumbers = input.filter { !it.contains("+") }

        val parsed = justNumbers.map { line ->
            var startIdx = 0
            val res = mutableListOf<String>()
            cheatingOnLastOne.forEach { next ->
                val endIdx = Math.min(next - 1, line.length)
                res.add(line.substring(startIdx, endIdx))
                startIdx = next
            }
            res
        }

        val flipped = flipMatrix(parsed)
        val nightmare = flipped.map { neededSeparateMethodForThisNightmare(it) }
        val operations =
            input.first { it.contains("+") }.split(" ").filter { it.isNotBlank() }.map { Operation.ofSymbol(it) }
        val calculated = nightmare.zip(operations).map { it.first.reduce(it.second.op) }
        val sum = calculated.sumOf { it }

        println(sum)
    }

    fun <T> flipMatrix(matrix: List<List<T>>): List<List<T>> {
        val flipped = mutableListOf<List<T>>()
        for (column in 0..matrix.first().size - 1) {
            val n = mutableListOf<T>()
            for (row in 0..matrix.size - 1) {
                n.add(matrix[row][column])
            }
            flipped.add(n)
        }
        return flipped
    }

    fun neededSeparateMethodForThisNightmare(input: List<String>): List<Long> {
        val split = input.map { it.split("").filter { !it.isEmpty() } }
        val result = mutableListOf<Long>()
        val maxLength = input.maxBy { it.length }.length
        for (i in maxLength - 1 downTo 0) {
            var numAsString = ""
            split.forEach { line ->
                if (i < line.size && line[i].isNotBlank()) {
                    numAsString += line[i]
                }
            }
            result.add(numAsString.toLong())
        }
        return result
    }

    enum class Operation(val symbol: String, val op: (Long, Long) -> Long) {
        ADD("+", Long::plus),
        MULTIPLY("*", Long::times);

        fun doOp(a: Long, b: Long): Long {
            return op(a, b)
        }

        companion object {
            fun ofSymbol(symbol: String): Operation {
                return Operation.entries.first { it.symbol == symbol }
            }
        }

    }


}

