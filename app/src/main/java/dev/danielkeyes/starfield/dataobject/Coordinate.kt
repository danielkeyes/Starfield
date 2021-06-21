package dev.danielkeyes.starfield.dataobject

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

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

    override fun toString(): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        df.format(x)
        return "(${x.format()},${y.format()})"    }
}

// TODO move to better location
/**
 * Return a string of double to two decimal places rounded to ceiling. ie. 4.232134 returns 4.23
 */
fun Double.format(): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING

    return df.format(this)
}

