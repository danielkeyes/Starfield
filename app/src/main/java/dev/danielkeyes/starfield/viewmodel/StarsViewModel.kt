package dev.danielkeyes.starfield.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.danielkeyes.starfield.dataobject.MovingStar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StarsViewModel : ViewModel() {

    var stars = mutableStateListOf<MovingStar>()
        private set

    private var _running: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val running: LiveData<Boolean>
        get() = _running

    var job: Job

    private var delay = 100L
    private var distance = 10.0

    init {
        // Starts the animation
        job = viewModelScope.launch {
            while (true) {
                moveStars(distance)
                delay(delay)
            }
        }
    }

    fun pause() {
        job.cancel() // this isn't immediate, but no ill effect
        _running.value = false
    }

    fun unpause() {
        job = viewModelScope.launch {
            while (true) {
                moveStars(distance)
                delay(delay)
            }
        }
        _running.value = true
    }

    private fun moveStars(distance: Double) {
        for (star in stars) {
            star.travel(distance)
        }
        // Workaround because Compose won't update if I don't modify the stars
        if (hasStars()) {
            stars.add(stars.get(0))
            stars.removeAt(0)
        }
    }

    fun addStar(star: MovingStar) {
        stars.add(star)
    }

    fun clearStars() {
        stars.clear()
    }

    private fun hasStars() = stars.isNotEmpty()
}