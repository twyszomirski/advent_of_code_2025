package org.twyszomirski.aoc.solutions

import java.io.File

class Day_7 {

    fun solve() {
        val lines = File("src/main/resources/input_day_7.txt").readLines()
        println("======== Day 7 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val matrix = input.mapIndexed { rowIdx, row ->
            row.split("").filter { it.isNotBlank() }
                .mapIndexed { columnIdx, symbol -> MatrixPoint(rowIdx, columnIdx, symbol) }
        }

        val startIdx = matrix.first().find { it.symbol == "S" }?.column!!

        var mask = Array(matrix.first().size) { false }
        mask[startIdx] = true

        var splitCount = 0
        matrix.drop(1).forEach { row ->
            val newMask = Array(matrix.first().size) { false }
            row.forEachIndexed { idx, point ->
                if (mask[idx]) {
                    if (point.symbol == "^") {
                        splitCount++
                        if (idx > 0) {
                            newMask[idx - 1] = true
                        }
                        if (idx < matrix.first().size - 1) {
                            newMask[idx + 1] = true
                        }
                    } else {
                        newMask[idx] = true
                    }
                }
            }
            mask = newMask
        }

        println(splitCount)
    }


    fun part2(input: List<String>) {
        val matrix = input.mapIndexed { rowIdx, row ->
            row.split("").filter { it.isNotBlank() }
                .mapIndexed { columnIdx, symbol -> MatrixPoint(rowIdx, columnIdx, symbol) }
        }

        val startIdx = matrix.first().find { it.symbol == "S" }?.column!!
        val rowLength = matrix.first().size
        var mask = Array(rowLength) { 0L }
        mask[startIdx] = 1

        matrix.drop(1).forEach { row ->
            val newMask = Array(rowLength) { 0L }
            row.forEachIndexed { idx, point ->
                if (mask[idx] > 0) {
                    if (point.symbol == "^") {
                        if (idx > 0) {
                            newMask[idx - 1] += mask[idx]
                        }
                        if (idx < rowLength - 1) {
                            newMask[idx + 1] += mask[idx]
                        }
                    } else {
                        newMask[idx] += mask[idx]
                    }
                }
            }
            mask = newMask
        }

        println(mask.sum())
    }


    data class MatrixPoint(val row: Int, val column: Int, val symbol: String) {

    }

}