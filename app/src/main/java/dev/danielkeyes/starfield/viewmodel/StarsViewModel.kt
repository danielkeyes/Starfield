package dev.danielkeyes.starfield.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dev.danielkeyes.starfield.dataobject.MovingStar

class StarsViewModel: ViewModel() {

    var stars = mutableStateListOf<MovingStar>()
        private set

    fun moveStars(distance: Double) {
        for (star in stars) {
            star.travel(distance)
        }
        // Workaround because Compose won't update if I don't modify the stars
        // Follow state compose codelab, but that didn't handle this
        stars.add(stars.get(0))
        stars.removeAt(0)
    }

    fun addStar(star: MovingStar){
        stars.add(star)
    }

    fun clearStars(){
        stars.clear()
    }

    fun hasStars(): Boolean {
        return stars.isNotEmpty()
    }

    fun printStarsLog() {
        stars.forEach { star ->
            star.printToLog()
        }
    }
}