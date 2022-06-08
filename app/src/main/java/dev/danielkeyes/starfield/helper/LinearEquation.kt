package dev.danielkeyes.starfield.helper

import dev.danielkeyes.starfield.dataobject.Coordinate

class LinearEquation {
    companion object {
        fun calculateSlope(coordinate1: Coordinate, coordinate2: Coordinate): Double {
            return calculateSlope(coordinate1.x, coordinate1.y, coordinate2.x, coordinate2.y)
        }

        private fun calculateSlope(x1: Double, y1: Double, x2: Double, y2: Double,): Double {
            // workaround for undefined
            if(x2 == x1) {
                return 1000.0
            }
            return ((y2 - y1) / (x2 - x1))
        }

        fun calculateYIntercept(coordinate: Coordinate, slope: Double): Double {
            return calculateYIntercept(coordinate.x, coordinate.y, slope)
        }

        private fun calculateYIntercept(x: Double, y: Double, slope: Double): Double {
            return y - (slope * x)
        }
    }
}