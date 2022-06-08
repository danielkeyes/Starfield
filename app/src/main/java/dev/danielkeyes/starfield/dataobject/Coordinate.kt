package dev.danielkeyes.starfield.dataobject

import dev.danielkeyes.starfield.helper.toTwoDecimals
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * X and Y coordinate in two dimensional space
 * @param x x-axis value
 * @param y y-axis value
 */
class Coordinate(val x: Double, val y: Double) {
    /**
     * Calculates distance to [Coordinate]
     */
    fun distance(coordinate: Coordinate): Double {
        return sqrt((coordinate.x - this.x).pow(2) + abs(coordinate.y - this.y).pow(2))
    }

    fun equals(coordinate: Coordinate): Boolean {
        if (this.x == coordinate.x && this.y == coordinate.y) {
            return true
        }
        return false
    }

    override fun toString() = "(${x.toTwoDecimals()},${y.toTwoDecimals()})"
}


