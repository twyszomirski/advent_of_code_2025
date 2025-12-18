package org.twyszomirski.aoc.solutions

import java.io.File

class Day_4 {

    fun solve() {
        val lines = File("src/main/resources/input_day_4.txt").readLines()
        println("======== Day 4 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val matrix = input.mapIndexed { rowIdx, row ->
            row.split("").filter { it.isNotBlank() }.mapIndexed { colIdx, col -> SymbolPosition(rowIdx, colIdx, col) }
        }

        val directions = listOf(
            0 to 1, 0 to -1, 1 to 0, -1 to 0,
            -1 to -1, 1 to -1, -1 to 1, 1 to 1
        )

        val count = matrix.flatten().count { position ->
            position.symbol == "@" &&
                    directions.map { dir -> hasSymbol(matrix, position, dir, "@") }.count { it } < 4
        }

        println(count)

    }

    private fun hasSymbol(
        matrix: List<List<SymbolPosition>>,
        current: SymbolPosition,
        direction: Pair<Int, Int>,
        symbol: String
    ): Boolean {
        val newRow = current.row + direction.first
        val newColumn = current.column + direction.second
        if (newRow >= 0 && newRow < matrix.size && newColumn >= 0 && newColumn < matrix.first().size) {
            return matrix[newRow][newColumn].symbol == symbol
        } else return false
    }


    fun part2(input: List<String>) {
        val matrix = input.mapIndexed { rowIdx, row ->
            row.split("").filter { it.isNotBlank() }.mapIndexed { colIdx, col -> SymbolPosition(rowIdx, colIdx, col) }
        }
        val directions = listOf(
            0 to 1, 0 to -1, 1 to 0, -1 to 0,
            -1 to -1, 1 to -1, -1 to 1, 1 to 1
        )
        var removedCount = 0
        var totalRemoved = 0
        do {
            removedCount = 0
            matrix.flatten().forEach { pos ->
                if (pos.symbol == "@" && directions.map { dir -> hasSymbol(matrix, pos, dir, "@") }.count { it } < 4) {
                    pos.symbol = "x"
                    removedCount++
                }
            }
            totalRemoved += removedCount;

        } while (removedCount > 0)

        println(totalRemoved)
    }


    data class SymbolPosition(val row: Int, val column: Int, var symbol: String)

}

