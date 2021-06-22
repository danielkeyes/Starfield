package dev.danielkeyes.starfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.danielkeyes.starfield.dataobject.Coordinate
import dev.danielkeyes.starfield.dataobject.MovingStar
import dev.danielkeyes.starfield.dataobject.format

@Composable
fun StarField(
    stars: List<MovingStar>,
    modifier: Modifier = Modifier,
    starStarSize: Dp = 3.dp,
    starEndSize: Dp = 25.dp,
    showDiagnostics: Boolean = false,
) {
    BoxWithConstraints(modifier = modifier) {
        val lengthX: Double = maxWidth.value.toDouble()
        val lengthY: Double = maxHeight.value.toDouble()

        // Display to show current values
        if (showDiagnostics) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "lengthX: ${lengthX.format()}")
                Text(text = "lengthY: ${lengthY.format()}")
            }
        }

        // could also not offset by .2
        stars.forEach { movingStar ->
            val currentStarSize =
                ((movingStar.getTravelPercent() / 100.0) * (starEndSize.value - starStarSize.value)).dp + starStarSize

            val minAlphaPercent: Double = 20.0
            val starAlpha = minOf(1.0, (movingStar.getTravelPercent()/100.0) + (minAlphaPercent/100) ).toFloat()

            Box(
                modifier = Modifier
                    .height(currentStarSize)
                    .width(currentStarSize)
                    .offset(movingStar.currentLocation.x.dp, movingStar.currentLocation.y.dp)
                    .clip(CutCornerShape(percent = 50))
                    .background(MaterialTheme.colors.onBackground.copy(alpha = starAlpha))
            ) {
            }
        }

        // TODO could draw black box over origin to stop blinking at origin if multiple stars there
        Box(
            modifier = Modifier
                .height(starStarSize)
                .width(starStarSize)
                .offset(stars.first().origin.x.dp, stars.first().origin.y.dp)
                .clip(CutCornerShape(percent = 50))
                .background(MaterialTheme.colors.background)
        ) {
        }
    }
}