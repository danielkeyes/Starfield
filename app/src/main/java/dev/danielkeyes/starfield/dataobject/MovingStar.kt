package dev.danielkeyes.starfield.dataobject

import androidx.compose.ui.unit.DpOffset
import dev.danielkeyes.starfield.Coordinate
import dev.danielkeyes.starfield.calculateSlope
import dev.danielkeyes.starfield.calculateYIntercept
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class MovingStar(
    val origin: Coordinate,
    var currentLocation: Coordinate,
    var offset: Double = 0.0,
) {
    private var slope: Double = 0.0
    private var yIntercept: Double = 0.0

    init {
        // if origin and currentLocation are the same, generate random slop and yIntercept
        if (origin == currentLocation) {
            // between -3 and 3
            val between0and5 = floor(Math.random() * 7) - 3
            slope = Math.random()
        } else {
            slope = calculateSlope(origin, currentLocation)
        }
        yIntercept = calculateYIntercept(origin, slope)
        offset = offset.rem(1.0)
    }

    /**
     * updates the current location based on given distance
     */
    fun travel( distance: Double) {
        val newX = this.currentLocation.x + sqrt( distance.pow(2.0) /(1 + slope.pow(2.0)))
        val newY = (slope * newX) + yIntercept
        this.currentLocation = Coordinate(newX, newY)
    }
}

public fun createRandomMovingStar(x: Double, y:Double, maxHeight: Double, maxWidth: Double) : MovingStar {
    return MovingStar(
        origin = Coordinate(0.0, 0.0),
        currentLocation = Coordinate(x = Math.random() * maxWidth, y = Math.random() * maxHeight),
        offset = Math.random(),
    )
}