package dev.danielkeyes.starfield.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.danielkeyes.starfield.R
import dev.danielkeyes.starfield.ui.theme.StarfieldTheme

@Composable
fun Introduction(
    done: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.starfieldfullscreen),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .background(color = Color.Black)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Starfield",
                Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.h3,
            )
            Text(text = "Controls", Modifier.padding(vertical = 4.dp))
            Text(text = "tap screen to pause and unpause")
            Text(text = "long press to launch options")
            Spacer(Modifier.height(8.dp))
            Button(onClick = { done() }) {
                Text(text = "Begin")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewIntroduction() {
    StarfieldTheme {
        Surface(color = MaterialTheme.colors.background) {
            Introduction(done = {})
        }
    }
}
