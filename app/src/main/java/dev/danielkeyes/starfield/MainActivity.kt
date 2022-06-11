package dev.danielkeyes.starfield

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.danielkeyes.starfield.screen.StarField
import dev.danielkeyes.starfield.screen.Introduction
import dev.danielkeyes.starfield.ui.theme.StarfieldTheme
import dev.danielkeyes.starfield.viewmodel.StarsViewModel
import dev.danielkeyes.starfield.viewmodel.StarsViewModel.Companion.INITIAL_SPEED

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()

            StarfieldTheme {
                Surface(color = MaterialTheme.colors.background) {
                    StarfieldNavHost(navController = navController, applicationContext)
                }
            }
        }
    }
}

@Composable
fun StarfieldNavHost(navController: NavHostController, applicationContext: Context) {
    NavHost(navController = navController, startDestination = "introduction"){
        composable("introduction"){
            Introduction(
                done = {
                    navController.navigate("starfield") {
                        popUpTo("introduction") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("starfield") {
            val viewModel = viewModel<StarsViewModel>()

            val starColor by viewModel.starColor.observeAsState()
            val speed by viewModel.speed.observeAsState(INITIAL_SPEED)

            StarField(
                stars = viewModel.stars,
                speed = speed,
                updateSpeed = {viewModel.updateSpeed(it) },
                starColor = starColor,
                pauseUnpause = { viewModel.pauseUnpause()},
            )
        }
    }
}