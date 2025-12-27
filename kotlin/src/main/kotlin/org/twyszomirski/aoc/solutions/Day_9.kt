package org.twyszomirski.aoc.solutions

import java.io.File
import kotlin.math.max
import kotlin.math.min

class Day_9 {

    fun solve() {
        val lines = File("src/main/resources/input_day_9.txt").readLines()
        println("======== Day 9 ===========")
        part1(lines)
        part2(lines)
    }

    fun part1(input: List<String>) {
        val points = input.map { it.split(",") }.map { Point(it[0].toLong(), it[1].toLong()) }
        val sortedPoints = points.sortedWith(compareBy<Point> { it.y }.thenBy { it.x })

        val pairs = sortedPoints.mapIndexed { idx, point ->
            sortedPoints.drop(idx + 1).map { next -> point to next }
        }.flatten()

        val areas = pairs.map { pair ->
            val vert = (pair.second.y - pair.first.y) + 1
            val horiz = (Math.max(pair.first.x, pair.second.x) - Math.min(pair.first.x, pair.second.x)) + 1
            vert * horiz
        }

        println(areas.max())
    }


    fun part2(input: List<String>) {
        val points = input.map { it.split(",") }.map { Point(it[0].toLong(), it[1].toLong()) }
        val mainArea = (points + points.first()).windowed(2, 1)
            .map { Section(it.first(), it.last()) }


        val sortedPoints = points.sortedWith(compareBy<Point> { it.y }.thenBy { it.x })
        val maxX = sortedPoints.maxBy { it.x }.x + 2
        val maxY = sortedPoints.maxBy { it.y }.y + 2
        val pairs = sortedPoints.mapIndexed { idx, point ->
            sortedPoints.drop(idx + 1).map { next -> point to next }
        }.flatten()

        val squares = pairs.map { pair ->
            val vert = (pair.second.y - pair.first.y) + 1
            val horiz = (Math.max(pair.first.x, pair.second.x) - Math.min(pair.first.x, pair.second.x)) + 1
            (vert * horiz) to Square.ofOppositePoints(pair)
        }.sortedBy { it.first }.reversed()

        val withExternalIntersections = squares.filter { hasAllIntersections(mainArea, it.second, maxX, maxY) }
        val withInternalIntersections = withExternalIntersections.filter { area ->
            !area.second.internalSections().any { section -> hasIntersection(mainArea, section) }
        }

        println(withInternalIntersections.first().first)
    }

    fun hasAllIntersections(area: List<Section>, square: Square, maxX: Long, maxY: Long): Boolean {
        return square.extensionSections(maxX, maxY).all { hasIntersection(area, it) }
    }

    fun hasIntersection(area: List<Section>, section: Section): Boolean {
        return area.any { doIntersect(it, section) }
    }


    data class Point(val x: Long, val y: Long)

    data class Section(val a: Point, val b: Point)

    data class Square(val top: Section, val right: Section, val bottom: Section, val left: Section) {

        fun internalSections(): List<Section> {
            val topY = Math.min(left.a.y, left.b.y)
            val bottomY = Math.max(left.a.y, left.b.y)
            val leftX = Math.min(top.a.x, top.b.x)
            val rightX = Math.max(top.a.x, top.b.x)

            val top = Section(Point(leftX + 1, topY + 1), Point(rightX - 1, topY + 1))
            val bottom = Section(Point(leftX + 1, bottomY - 1), Point(rightX - 1, bottomY - 1))
            val right = Section(Point(rightX - 1, topY + 1), Point(rightX - 1, bottomY - 1))
            val left = Section(Point(leftX + 1, topY + 1), Point(leftX + 1, bottomY - 1))

            return listOf(top, bottom, right, left)
        }

        fun extensionSections(maxX: Long, maxY: Long): List<Section> {

            val leftTop = Section(top.a, Point(top.a.x, 0))
            val rightTop = Section(top.b, Point(top.b.x, 0))
            val leftBottom = Section(bottom.b, Point(bottom.b.x, maxY))
            val rightBottom = Section(bottom.a, Point(bottom.a.x, maxY))

            val leftLeftTop = Section(top.a, Point(0, top.a.y))
            val rightRightTop = Section(top.b, Point(maxX, top.b.y))
            val leftLeftBottom = Section(Point(0, bottom.b.y), bottom.b)
            val rightRightBottom = Section(bottom.a, Point(maxX, bottom.a.y))

            return listOf(
                leftTop, rightTop, leftBottom, rightBottom,
                leftLeftTop, rightRightTop, leftLeftBottom, rightRightBottom
            )
        }

        companion object {
            fun ofOppositePoints(points: Pair<Point, Point>): Square {

                val topLeft = Point(
                    Math.min(points.first.x, points.second.x), points.first.y
                )

                val topRight = Point(
                    Math.max(points.first.x, points.second.x), points.first.y
                )

                val bottomLeft =
                    Point(topLeft.x, points.second.y)

                val bottomRight =
                    Point(topRight.x, points.second.y)

                return Square(
                    Section(topLeft, topRight),
                    Section(topRight, bottomRight),
                    Section(bottomRight, bottomLeft),
                    Section(bottomLeft, topLeft)
                )

            }
        }
    }

    fun orientation(p: Point, q: Point, r: Point): Int {
        val result = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y)

        if (result == 0L) return 0
        return if (result > 0) 1 else 2
    }

    fun onSegment(p: Point, q: Point, r: Point): Boolean {
        return (q.x <= max(p.x, r.x) && q.x >=
                min(p.x, r.x) && q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y))
    }

    fun doIntersect(aa: Section, bb: Section): Boolean {
        val o1 = orientation(aa.a, aa.b, bb.a)
        val o2 = orientation(aa.a, aa.b, bb.b)
        val o3 = orientation(bb.a, bb.b, aa.a)
        val o4 = orientation(bb.a, bb.b, aa.b)

        if (o1 != o2 && o3 != o4) return true

        if (o1 == 0 &&
            onSegment(aa.a, bb.a, aa.b)
        ) return true

        if (o2 == 0 &&
            onSegment(aa.a, bb.b, aa.b)
        ) return true

        if (o3 == 0 &&
            onSegment(bb.a, aa.a, bb.b)
        ) return true

        if (o4 == 0 &&
            onSegment(bb.a, aa.b, bb.b)
        ) return true

        return false
    }

}