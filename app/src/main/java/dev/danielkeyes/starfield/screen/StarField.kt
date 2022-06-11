package dev.danielkeyes.starfield.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import dev.danielkeyes.starfield.dataobject.MovingStar
import dev.danielkeyes.starfield.helper.toTwoDecimals
import dev.danielkeyes.starfield.ui.theme.StarfieldTheme
import dev.danielkeyes.starfield.viewmodel.StarsViewModel.Companion.MAX_SPEED
import dev.danielkeyes.starfield.viewmodel.StarsViewModel.Companion.MIN_SPEED
import kotlin.math.roundToInt

private const val troubleshoot = false

@Composable
fun StarField(
    stars: List<MovingStar>,
    speed: Int,
    updateSpeed: (Int) -> Unit,
    pauseUnpause: () -> Unit,
    starColor: Int?,
) {
    var optionsVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Stars(
            stars = stars,
            starColor = starColor,
            onClick = { if (optionsVisible) optionsVisible = false else pauseUnpause() },
            onLongPress = { optionsVisible = true },
        )
        if (optionsVisible) {
            Options(
                speed = speed,
                updateSpeed = updateSpeed,
                done = { optionsVisible = false })
        }
    }
}

@Composable
private fun Stars(
    stars: List<MovingStar>,
    starColor: Int?,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    starStarSize: Dp = 3.dp,
    starEndSize: Dp = 25.dp,
) {
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = { onClick() }, onLongPress = { onLongPress() })
        }) {
        val lengthX: Double = maxWidth.value.toDouble()
        val lengthY: Double = maxHeight.value.toDouble()

        // Display to show current values
        if (troubleshoot) {
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
                1.0, (movingStar.getTravelPercent() / 100.0) + (minAlphaPercent / 100)
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

@Composable
fun Options(
    speed: Int,
    updateSpeed: (Int) -> Unit,
    done: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = Color.Black.copy(alpha = .8f))
            .clickable {}
            .padding(16.dp)
    ) {
        Text(
            text = "Options",
            Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.h4,
        )


        Row {
            Text("Speed")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = speed.toString())
        }
        Slider(
            value = (speed.toFloat()),
            onValueChange = { updateSpeed(it.toInt()) },
            valueRange = MIN_SPEED.toFloat()..MAX_SPEED.toFloat(),
            modifier = Modifier.padding(end = 32.dp)
        )


        Text(
            text = "Unimplemented",
            Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.h5,
        )
        // TODO implement in the future
        var starCountSliderPosition by remember { mutableStateOf(50f) }
        Row {
            Text("Star Count")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = starCountSliderPosition.toString())
        }
        Slider(
            value = starCountSliderPosition,
            onValueChange = { starCountSliderPosition = it },
            valueRange = 50F..50F,
            modifier = Modifier.padding(end = 32.dp)
        )

        // Star Color
        Text("Star Color")
//        var selected by remember { mutableStateOf("Color") }
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(selected = selected == "Color", onClick = { selected = "Color" })
//            Text(
//                text = "Color",
//                modifier = Modifier
//                    .clickable(onClick = { selected = "Color" })
//                    .padding(start = 4.dp)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            RadioButton(selected = selected == "Rainbow", onClick = { selected = "Rainbow" })
//            Text(
//                text = "Rainbow",
//                modifier = Modifier
//                    .clickable(onClick = { selected = "Rainbow" })
//                    .padding(start = 4.dp)
//            )
//        }
        var color by remember { mutableStateOf(Color.White) }
        Box(
            Modifier
                .background(color)
                .height(40.dp)
                .width(120.dp)
                .clickable {
                    color = if (color == Color.White) Color.Magenta else Color.Cyan
                }) {}
    }
}

@Preview
@Composable
private fun PreviewOptions() {
    StarfieldTheme {
        Surface(color = MaterialTheme.colors.background) {
            Options(1, {}) {}
        }
    }
}