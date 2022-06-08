package dev.danielkeyes.starfield.helper

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Return a string of double to two decimal places rounded to ceiling. ie. 4.232134 returns 4.23
 */
fun Double.toTwoDecimals(): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING

    return df.format(this)
}
