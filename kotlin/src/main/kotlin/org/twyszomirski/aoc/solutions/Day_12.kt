package org.twyszomirski.aoc.solutions

import java.io.File

class Day_12 {

    fun solve() {
        val lines = File("src/main/resources/input_day_12.txt").readLines()
        println("======== Day 12 ===========")
        part1(lines)
    }

    fun part1(input: List<String>) {
        val (dimensions, _) = input.partition { it.contains("x") }
        val areas = dimensions.map { it.split(":") }.map { it[0] to it[1] }
            .map {
                Area(
                    it.first.split("x")[0].toInt(), it.first.split("x")[1].toInt(),
                    it.second.split(" ").filter { it.isNotBlank() }.map { it.toInt() })
            }

        val areasThatFit = areas.map { area ->
            val totalSlots = (area.width/3) * (area.height/3)
            totalSlots>= area.presents.sum()
        }.filter { it }

        println(areasThatFit.count())
    }

    data class Area(val width: Int, val height: Int, val presents: List<Int>)

}