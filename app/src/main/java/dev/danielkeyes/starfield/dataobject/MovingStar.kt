package dev.danielkeyes.starfield.dataobject

import android.util.Log
import dev.danielkeyes.starfield.LinearEquation
import kotlin.math.pow
import kotlin.math.sqrt

private enum class Direction {
    left, right
}

class MovingStar(
    val origin: Coordinate,
    val destination: Coordinate,
    var initialOffset: Double = 0.0,
    private val repeat: Boolean = true,
) {
    private var slope: Double = 0.0
    private var yIntercept: Double = 0.0
    private var direction: Direction = Direction.left
    var currentLocation: Coordinate = Coordinate(origin.x, origin.y)

    init {
        slope = LinearEquation.calculateSlope(origin, destination)
        yIntercept = LinearEquation.calculateYIntercept(origin, slope)

        initialOffset = initialOffset.rem(1.0)

        direction = if(origin.x < destination.x) {
            Direction.right
        } else {
            Direction.left
        }

        travel(origin.distance(destination) * initialOffset)
    }

    /**
     * Updates the current location based on given distance. Calculates new X and Y coordinate.
     * Travels on linear path based on origin and destination coordinates.
     * If travel past destination, reset to origin
     */
    fun travel( distance: Double) {
        // Could pull some of this into LinearEquation
        val directionModifier = if (Direction.left == direction) -1 else 1
        val xDistance =  sqrt(distance.pow(2.0) / (1 + slope.pow(2.0)))

        var newX: Double = this.currentLocation.x + (directionModifier * xDistance)

        val newY = (slope * newX) + yIntercept

        // If we haven't traveled past destination, use new coordinates
        if(!pastDestination(Coordinate(newX, newY))) {
            this.currentLocation = Coordinate(newX, newY)
        }
        else { // else reset to origin
            this.currentLocation = Coordinate(origin.x,origin.y)
        }
    }

    /**
     * Determines if newDestination is past MovingStars destination
     */
    private fun pastDestination(newDestination: Coordinate) : Boolean {
        return if (direction == Direction.right) { // Right
            newDestination.x > destination.x  // || newDestination.y > destination.y
        } else {
            newDestination.x < destination.x // || newDestination.y < destination.y
        }
    }

    /**
     * Determines how far star has traveled to destination
     *
     * @return percent 0-100 of travel distance
     */
    fun getTravelPercent(): Double {
        return ((currentLocation.x - origin.x)/(destination.x - origin.x)) * 100.0
    }

    override fun toString(): String {
        return "MovingStar(origin=${origin.toString()}, " +
                "destination=${destination.toString()}, " +
                "offset=$initialOffset, " +
                "repeat=$repeat, " +
                "slope=$slope, " +
                "yIntercept=$yIntercept, " +
                "currentLocation=${currentLocation.toString()})"
    }

    fun printToLog() {
        Log.e("MovingStar", this.toString())
    }
}

fun createRandomMovingStarInCircle(radius: Double, center: Coordinate): MovingStar {
    val radiusModifier = 1.05 // .3 allows us to see all moving stars on screen
    val destination: Coordinate =
        getRandomPointOnCircle(center = center, radius = radius * radiusModifier)
    return MovingStar(
        origin = Coordinate(
            center.x,
            center.y
        ), destination = destination,
        initialOffset = Math.random()
    )
}

/**
 * Given circle center coordinate and radius, generate a random point on the circle
 */
fun getRandomPointOnCircle(center: Coordinate, radius: Double): Coordinate {
    var angle = Math.random() * Math.PI * 2;

    val x = (Math.cos(angle) * (radius)) + center.x;
    val y = (Math.sin(angle) * (radius)) + center.y;

    return Coordinate(x, y)
}