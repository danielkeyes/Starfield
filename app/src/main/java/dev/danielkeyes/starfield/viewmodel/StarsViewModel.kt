package dev.danielkeyes.starfield.viewmodel

import android.app.Application
import android.graphics.Color
import android.util.DisplayMetrics
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.danielkeyes.starfield.dataobject.Coordinate
import dev.danielkeyes.starfield.dataobject.MovingStar
import dev.danielkeyes.starfield.dataobject.createRandomMovingStarInCircle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StarsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MIN_STARS = 10
        const val MAX_STARS = 200
        const val INITIAL_STARS = 100

        const val MIN_SPEED = 1
        const val MAX_SPEED = 100
        const val INITIAL_SPEED = 30
    }

    var stars = mutableStateListOf<MovingStar>()
        private set

    private var _running: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val running: LiveData<Boolean>
        get() = _running

    private var _starColor: MutableLiveData<Int> = MutableLiveData<Int>(Color.WHITE)
    val starColor: LiveData<Int>
        get() = _starColor

    private var _speed: MutableLiveData<Int> = MutableLiveData<Int>(INITIAL_SPEED)
    val speed: LiveData<Int>
        get() = _speed

    private val _starCount: MutableLiveData<Int> = MutableLiveData<Int>(100)
    val starCount: LiveData<Int>
        get() = _starCount

    // TODO
//    enum class StarShape {
//        DIAMOND, ROUND, SQUARE
//    }

    private var job: Job? = null

    private var delay = 100L
    private var distance = 25.0

    init {
        val metrics: DisplayMetrics = application.applicationContext.resources.displayMetrics
        val width: Double = (metrics.widthPixels / metrics.density).toDouble()
        val height: Double = (metrics.heightPixels / metrics.density).toDouble()

        val middleCoordinate = Coordinate(x = width / 2.0, y = height / 2.0)
        val radius = middleCoordinate.distance(Coordinate(width, height))

        val starAmount = _starCount.value ?: 100
        repeat(starAmount) {
            addStar(createRandomMovingStarInCircle(radius, middleCoordinate))
        }

        unpause()

        updateSpeed(_speed.value ?: INITIAL_SPEED)
    }

    fun pauseUnpause() {
        if (running.value == true) {
            pause()
        } else {
            unpause()
        }
    }

    /**
     * Update star count between [MIN_STARS] and [MAX_STARS]
     * @param starCount stars wanted, round up or down for range
     */
    fun updateStarCount(starCount: Int) {
        _starCount.value = if (starCount < MIN_STARS) {
            MIN_STARS
        } else if (starCount in MIN_STARS..MAX_STARS) {
            starCount
        } else {
            MAX_STARS
        }
    }

    fun updateSpeed(speed: Int) {
        _speed.value = if (speed < MIN_SPEED) {
            MIN_SPEED
        } else if (speed in MIN_SPEED..MAX_SPEED) {
            speed
        } else {
            MAX_SPEED
        }

        updateDelay(_speed.value ?: INITIAL_SPEED)
    }

    private fun updateDelay(speed:Int){
        val minDelay = 15
        val maxDelay = 200

        val steps = maxDelay-minDelay //85 steps

        delay = maxDelay - (speed * steps.toFloat()/100).toLong()
    }

    private fun pause(){
        job?.cancel()
        _running.value = false
    }

    private fun unpause() {
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