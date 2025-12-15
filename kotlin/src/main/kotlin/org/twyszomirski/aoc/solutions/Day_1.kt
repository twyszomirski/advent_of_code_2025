package org.twyszomirski.aoc.solutions

import java.io.File

class Day_1 {

    fun solve() {
        val lines = File("src/main/resources/input_day_1.txt").readLines()
        println("======== Day 1 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val rotations = input.map { Rotation.ofLiterals(it.substring(0, 1), it.substring(1)) }

        val rotated = rotations.runningFold(50 to 0) { acc, rotation -> rotation.rotate(acc.first) }

        println(rotated.count { it.first == 0 })
    }

    fun part2(input: List<String>) {
        val rotations = input.map { Rotation.ofLiterals(it.substring(0, 1), it.substring(1)) }

        val rotated = rotations.runningFold(50 to 0) { acc, rotation -> rotation.rotate(acc.first) }

        println(rotated.count { it.first == 0 } + rotated.sumOf { it.second })
    }

    data class Rotation(val direction: Dir, val amount: Int) {
        companion object {
            fun ofLiterals(dir: String, amount: String): Rotation {
                return Rotation(if (dir == "L") Dir.LEFT else Dir.RIGHT, amount.toInt())
            }
        }

        fun rotate(current: Int): Pair<Int, Int> {
            return when (direction) {
                Dir.LEFT -> rotateLeft(current, amount, 0)
                Dir.RIGHT -> rotateRight(current, amount, 0)
            }
        }

        fun rotateLeft(current: Int, sub: Int, count: Int): Pair<Int, Int> {
            if (sub == 0) {
                return current to count
            }
            if (sub > current) {
                val leftOver = sub - current;
                return rotateLeft(100, leftOver, if (current == 0) count else count + 1)
            } else {
                return (current - sub) to count
            }
        }

        fun rotateRight(current: Int, add: Int, count: Int): Pair<Int, Int> {
            if (add == 0) {
                return current to count
            }
            if (add >= (100 - current)) {
                val leftOver = add - (100 - current)
                return rotateRight(0, leftOver, if (leftOver == 0) count else count + 1)
            } else {
                return (current + add) to count
            }
        }
    }

    enum class Dir {
        LEFT,
        RIGHT
    }
}