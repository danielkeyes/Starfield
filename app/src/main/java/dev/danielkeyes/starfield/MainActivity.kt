package dev.danielkeyes.starfield

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dev.danielkeyes.starfield.dataobject.Coordinate
import dev.danielkeyes.starfield.dataobject.createRandomMovingStarInCircle
import dev.danielkeyes.starfield.ui.theme.StarfieldTheme
import dev.danielkeyes.starfield.viewmodel.StarsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val scope = MainScope()

var job: Job? = null

const val starAmount: Int = 100

class MainActivity : ComponentActivity() {

    private val starsViewModel by viewModels<StarsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val metrics: DisplayMetrics = applicationContext.resources.displayMetrics
        val width: Double = (metrics.widthPixels / metrics.density).toDouble()
        val height:Double = (metrics.heightPixels  / metrics.density).toDouble()

        val middleCoordinate = Coordinate(x = width/2.0, y =height/2.0)
        val radius = middleCoordinate.distance(Coordinate(width, height))

        starsViewModel.clearStars()
        repeat(starAmount) {
            starsViewModel.addStar(createRandomMovingStarInCircle(radius, middleCoordinate))
        }

        startUpdates(starsViewModel)

        setContent {
            StarfieldTheme {
                Surface(color = MaterialTheme.colors.background) {
                    StarField(
                        starsViewModel.stars,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopUpdates()
    }
}



fun startUpdates(starsViewModel: StarsViewModel) {
    stopUpdates()
    job = scope.launch {
        while(true) {
            starsViewModel.moveStars(10.0)
            delay(80)
        }
    }
}

fun stopUpdates() {
    job?.cancel()
    job = null
}
