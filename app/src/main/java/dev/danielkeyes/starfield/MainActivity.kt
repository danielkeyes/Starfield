package dev.danielkeyes.starfield

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import dev.danielkeyes.starfield.composable.StarField
import dev.danielkeyes.starfield.dataobject.Coordinate
import dev.danielkeyes.starfield.dataobject.createRandomMovingStarInCircle
import dev.danielkeyes.starfield.ui.theme.StarfieldTheme
import dev.danielkeyes.starfield.viewmodel.StarsViewModel

const val starAmount: Int = 100

class MainActivity : ComponentActivity() {

    private val starsViewModel by viewModels<StarsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val metrics: DisplayMetrics = applicationContext.resources.displayMetrics
        val width: Double = (metrics.widthPixels / metrics.density).toDouble()
        val height: Double = (metrics.heightPixels / metrics.density).toDouble()

        val middleCoordinate = Coordinate(x = width / 2.0, y = height / 2.0)
        val radius = middleCoordinate.distance(Coordinate(width, height))

        starsViewModel.clearStars()
        repeat(starAmount) {
            starsViewModel.addStar(createRandomMovingStarInCircle(radius, middleCoordinate))
        }

        setContent {
            val isRunning by starsViewModel.running.observeAsState(false)

            StarfieldTheme {
                Surface(color = MaterialTheme.colors.background) {
                    StarField(
                        starsViewModel.stars,
                        { starsViewModel.pause() },
                        { starsViewModel.unpause() },
                        isAnimating = isRunning,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
