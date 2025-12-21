package org.twyszomirski.aoc.solutions

import java.io.File
import java.util.PriorityQueue
import kotlin.math.cbrt
import kotlin.math.pow

class Day_8 {

    fun solve() {
        val lines = File("src/main/resources/input_day_8.txt").readLines()
        println("======== Day 8 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val junctions = input.map { it.split(",") }
            .mapIndexed { idx, split -> Junction(split[0].toInt(), split[1].toInt(), split[2].toInt(), idx) }

        val distances =
            junctions.mapIndexed { idx, j -> junctions.drop(idx + 1).map { innerJ -> Distance.of(j, innerJ) } }

        val minHeap = PriorityQueue<Distance>(Comparator.comparing { it.dist })
        minHeap.addAll(distances.flatten())

        val n = 1000
        for (i in 0..<n) {
            val closest = minHeap.poll()
            with(closest) {
                b.circuit.junctions.forEach { j ->
                    a.circuit.add(j)
                    j.circuit = a.circuit
                }
                a.circuit.add(b)
                a.circuit.add(a)
                b.circuit = a.circuit
            }
        }

        val topCircuits = junctions.map { it.circuit }.distinct().sortedBy { it.junctions.size }
            .reversed().take(3).map { it.junctions.size }
        val mul = topCircuits.reduce(Int::times)
        println(mul)
    }


    fun part2(input: List<String>) {
        val junctions = input.map { it.split(",") }
            .mapIndexed { idx, split -> Junction(split[0].toInt(), split[1].toInt(), split[2].toInt(), idx) }

        val distances =
            junctions.mapIndexed { idx, j -> junctions.drop(idx + 1).map { innerJ -> Distance.of(j, innerJ) } }

        val minHeap = PriorityQueue<Distance>(Comparator.comparing { it.dist })
        minHeap.addAll(distances.flatten())

        val n = 1000000
        var distanceFromWall = 0

        for (i in 0..<n) {
            val closest = minHeap.poll()
            with(closest) {
                b.circuit.junctions.forEach { j ->
                    a.circuit.add(j)
                    j.circuit = a.circuit
                }
                a.circuit.add(b)
                a.circuit.add(a)
                b.circuit = a.circuit
            }
            if (closest.a.circuit.junctions.size == junctions.size) {
                distanceFromWall = closest.a.x * closest.b.x
                break
            }
        }

        println(distanceFromWall)
    }

    data class Junction(val x: Int, val y: Int, val z: Int, val idx: Int, var circuit: Circuit = Circuit()) {
        override fun equals(other: Any?): Boolean {
            return this.idx.equals((other as Junction).idx)
        }

        override fun hashCode(): Int {
            return idx.hashCode()
        }

        override fun toString(): String {
            return idx.toString()
        }
    }

    data class Distance(val a: Junction, val b: Junction, val dist: Double) {

        companion object {
            fun of(a: Junction, b: Junction): Distance {
                val dist = cbrt(
                    ((a.x - b.x).toDouble().pow(2.0)) + ((a.y - b.y).toDouble().pow(2.0))
                            + ((a.z - b.z).toDouble().pow(2.0))
                )
                return Distance(a, b, dist)
            }
        }
    }

    data class Circuit(val junctions: MutableSet<Junction> = mutableSetOf()) {
        fun add(j: Junction) {
            junctions.add(j)
        }
    }

}