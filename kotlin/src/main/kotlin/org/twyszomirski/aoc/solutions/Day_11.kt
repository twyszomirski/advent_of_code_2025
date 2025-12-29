package org.twyszomirski.aoc.solutions

import java.io.File

class Day_11 {

    fun solve() {
        val lines = File("src/main/resources/input_day_11.txt").readLines()
        println("======== Day 11 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val devices = parseDevices(input)
        val start = devices.first { it.name == "you" }

        val paths = findPath(start, emptySet())
        println(paths.size)
    }


    fun part2(input: List<String>) {
        val devices = parseDevices(input)
        val svr = devices.first { it.name == "svr" }
        val fft = devices.first { it.name == "fft" }
        val dac = devices.first { it.name == "dac" }
        val out = devices.first { it.name == "out" }

        val sum = (countPath(svr, fft) * countPath(fft, dac) * countPath(dac, out)) +
                (countPath(svr, dac) * countPath(dac, fft) * countPath(fft, out))

        println(sum)
    }

    fun findPath(device: Device, visited: Set<Device>): List<Set<Device>> {
        if (visited.contains(device)) {
            return emptyList()
        }
        if (device.name == "out") {
            return listOf(visited)
        }

        val paths = mutableListOf<Set<Device>>()
        device.neighbours.forEach { n ->
            val visitedCopy = visited + device
            paths.addAll(findPath(n, visitedCopy))
        }
        val result = paths.filterNot { it.isEmpty() }
        return result
    }

    fun countPath(from: Device, to: Device, cache: MutableMap<String, Long> = mutableMapOf()): Long {

        if (from.name == to.name) {
            return 1L
        }

        if (cache.contains(from.name)) {
            return cache[from.name]!!
        }

        val res = from.neighbours.sumOf { countPath(it, to, cache) }
        cache[from.name] = res
        return res
    }

    fun parseDevices(input: List<String>): List<Device> {
        val devices = input.map { it.split(":") }
            .map { Device(it[0]) to it[1].split(" ").filter { it.isNotBlank() }.toList() }
        val withOut = devices + (Device("out") to emptyList())
        val lookup = withOut.associateBy { it.first.name }
        return devices.map { (device, neighbours) ->
            device.neighbours = neighbours.map { lookup[it]?.first!! }.toList()
            device
        } + Device("out")
    }

    data class Device(val name: String, var neighbours: List<Device> = emptyList()) {
        override fun toString(): String {
            return name
        }

        override fun equals(other: Any?): Boolean {
            return name.equals((other as Device).name)
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }
    }

}