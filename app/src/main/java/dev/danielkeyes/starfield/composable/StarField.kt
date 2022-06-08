package dev.danielkeyes.starfield.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.danielkeyes.starfield.dataobject.MovingStar
import dev.danielkeyes.starfield.helper.toTwoDecimals

@Composable
fun StarField(
    stars: List<MovingStar>,
    pause: () -> Unit,
    unpause: () -> Unit,
    isAnimating: Boolean,
    modifier: Modifier = Modifier,
    starStarSize: Dp = 3.dp,
    starEndSize: Dp = 25.dp,
    showDiagnostics: Boolean = false,
) {
    BoxWithConstraints(modifier = modifier.clickable {
        if (isAnimating) pause() else unpause()
    }) {
        val lengthX: Double = maxWidth.value.toDouble()
        val lengthY: Double = maxHeight.value.toDouble()

        // Display to show current values
        if (showDiagnostics) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "lengthX: ${lengthX.toTwoDecimals()}")
                Text(text = "lengthY: ${lengthY.toTwoDecimals()}")
            }
        }

        stars.forEach { movingStar ->
            // size and brightness increasing the close to destination give a traveling affect
            // Stars are bigger the closer to destination
            val currentStarSize =
                ((movingStar.getTravelPercent() / 100.0) * (starEndSize.value - starStarSize
                    .value)).dp + starStarSize

            // Starts are brighter the closer to destination
            val minAlphaPercent: Double = 20.0
            val starAlpha = minOf(
                1.0,
                (movingStar.getTravelPercent() / 100.0) + (minAlphaPercent / 100)
            ).toFloat()

            // Draw the star
            Box(
                modifier = Modifier
                    .height(currentStarSize)
                    .width(currentStarSize)
                    .offset(movingStar.currentLocation.x.dp, movingStar.currentLocation.y.dp)
                    .clip(CutCornerShape(percent = 50))
                    .background(MaterialTheme.colors.onBackground.copy(alpha = starAlpha))
            ) {}
        }

        // Draw black box over origin to stop blinking at origin if multiple stars there
        Box(
            modifier = Modifier
                .height(starStarSize)
                .width(starStarSize)
                .offset(stars.first().origin.x.dp, stars.first().origin.y.dp)
                .clip(CutCornerShape(percent = 50))
                .background(MaterialTheme.colors.background)
        ) {}
    }
}