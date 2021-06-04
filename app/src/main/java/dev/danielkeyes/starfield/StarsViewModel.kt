package dev.danielkeyes.starfield

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.danielkeyes.starfield.dataobject.MovingStar

class StarsViewModel: ViewModel() {

    var stars by mutableStateOf(listOf<MovingStar>())
        private set

    fun moveStars(distance: Double) {
        for (star in stars) {
            star.travel(distance)
        }
    }

    fun addStar(star: MovingStar){
        stars = stars + listOf<MovingStar>(star)
    }

    fun clearStars(){
        stars = listOf<MovingStar>()
    }

}