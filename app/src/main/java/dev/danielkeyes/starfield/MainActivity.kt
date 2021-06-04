package dev.danielkeyes.starfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.danielkeyes.starfield.ui.theme.StarfieldTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.lerp
import dev.danielkeyes.starfield.dataobject.MovingStar
import dev.danielkeyes.starfield.dataobject.createRandomMovingStar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

val scope = MainScope() // could also use an other scope such as viewModelScope if available
var job: Job? = null

class MainActivity : ComponentActivity() {

    private val starsViewModel by viewModels<StarsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarfieldTheme {
                Surface(color = MaterialTheme.colors.background) {
                    StarFieldScreen(
                        starsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun StarFieldScreen(starsViewModel: StarsViewModel) {
    // start logic for timed updates
    val fraction = remember { mutableStateOf(0F)}
    var fps = 60L

    repeat(100) {
        starsViewModel.addStar(createRandomMovingStar(0.0, 0.0, 1.0, 1.0))
    }

    fun stopUpdates() {
        job?.cancel()
        job = null
    }

    fun startUpdates() {
        stopUpdates()
        job = scope.launch {
            while(true) {
                fraction.value += 0.01F
                //1.2 so it can be off screen for a bit before reappearing
                if(fraction.value > 1.2){ fraction.value = 0F}
                delay(1000/fps)
            }
        }
    }
    // TODO restart
//    startUpdates()
    // end logic for timed updates

    // start set up moving points
    var stars  = starsViewModel.stars

    // end set up of moving points

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()) {

        val maximumHeight: Dp = maxHeight
        val maximumWidth: Dp = maxWidth

        val middleX = maximumWidth / 2
        val middleY = maximumHeight / 2

        val maxDistance = sqrt(
            Math.pow((maximumWidth.value.toDouble() / 2), 2.0) +
                    Math.pow((maximumHeight.value.toDouble() / 2), 2.0)
        )

        // Displayed value top left
        Column(modifier = Modifier.padding(8.dp)) {
            Text("StarField ${fraction.value} ")
            Text("maxHeight: $maximumHeight")
            Text("maxWidth: $maximumWidth")
            Text("stars: ${stars.size}")
            for(i in 1..10){
                Text("stars: ${stars.get(i).currentLocation.x} , ${stars.get(i).currentLocation.y}")
            }
        }

        // Middle text box
        Column(
            modifier = Modifier
                .offset(x = maxWidth / 2, y = maxHeight / 2)
                .background(Color.Gray), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${fraction.value}")
            Text(text = "${lerp(middleX, maximumWidth, fraction.value)}")
            Text(text = "${lerp(middleY, maximumHeight, fraction.value)}")
        }

        val x =
            lerp(DpOffset(middleX, middleY), DpOffset(maximumWidth, maximumHeight), fraction.value)

        // A "Star"
        var xTarget =
            remember { mutableStateOf(value = maximumWidth.times(Math.random().toFloat())) }
        var yTarget =
            remember { mutableStateOf(value = maximumHeight.times(Math.random().toFloat())) }

        stars.forEach {
            Box(
                modifier = Modifier.height(2.dp).width(2.dp)
                    .background(MaterialTheme.colors.onBackground)
                    .offset(it.currentLocation.x.dp, it.currentLocation.y.dp)
            ) { }
        }

        Box(
            modifier = Modifier
                .offset(
                    x = lerp(middleX, xTarget.value, fraction.value),
                    y = lerp(middleY, yTarget.value, fraction.value)
                )
                .background(MaterialTheme.colors.onBackground)
                .width(2.dp)
                .height(2.dp)
        ) { }

        //Reference Points
        Text(text = "100, 100", modifier = Modifier.offset(x = 100.dp, y = 100.dp))
        Text(text = "-100, -100", modifier = Modifier.offset(x = (-100).dp, y = (-100).dp))
        Text(text = "300, 300", modifier = Modifier.offset(x = 300.dp, y = 300.dp))
    }

}

@Composable
fun MyText(xOffset:Dp, yOffset:Dp, text:String? = null) {
    Text(
        text ?: "${xOffset.toString()} ${yOffset.toString()}",
        color = Color.White,
        modifier = Modifier.offset(xOffset, yOffset)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StarfieldTheme {
        // TODO implement
    }
}


/**
 * Generates and random x,y coordinate as a Pair
 *
 * @return [Coordinate] between 0,0 and maxWidth, maxHeight
 */
fun getRandomPoint(maxWidth: Double, maxHeight: Double): Coordinate {
    return Coordinate( x = (maxWidth * Math.random()), y = (maxWidth * Math.random()))
}

/**
 * Calculates slope based off of two x,y coordinates
 *
 * @return slope
 */
fun calculateSlope(coordinate1: Coordinate, coordinate2: Coordinate): Double {
    return ( (coordinate2.y - coordinate1.y) / (coordinate2.x - coordinate1.x))
}

/**
 * Calculates Y Intercept based off of slope and x.y coordinate
 *
 * @return Y Intercept
 */
fun calculateYIntercept(coordinate: Coordinate, slope: Double): Double {
    return coordinate.y - (slope * coordinate.x)
}

/**
 * Calculates distance between two [Coordinate]s
 */
fun calculateDistance(coordinate1: Coordinate, coordinate2: Coordinate): Double {
    return sqrt( abs( coordinate2.x - coordinate1.x ) + abs( coordinate2.y - coordinate1.y))
}

/**
 *
 * @return determines in position is outside of container with maxWidth and maxHeight
 */
fun isOutsideContainer(
    position: Coordinate,
    containerMaxWidth: Float,
    containerMaxHeight: Float
): Boolean {
    if (position.x < 0 || position.x > containerMaxWidth || position.y < 0 || position.y > containerMaxHeight) {
        return false
    }
    return true
}

class Coordinate(val x: Double, val y:Double) {
    public fun equals(coordinate: Coordinate):Boolean {
        if (this.x == coordinate.x && this.y == coordinate.y) {
            return true
        }
        return false
    }
}

