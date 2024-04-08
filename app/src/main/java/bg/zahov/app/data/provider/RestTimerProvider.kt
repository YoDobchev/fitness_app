package bg.zahov.app.data.provider

import android.os.CountDownTimer
import android.util.Log
import bg.zahov.app.data.interfaces.RestProvider
import bg.zahov.app.data.model.RestState
import bg.zahov.app.util.parseTimeStringToLong
import bg.zahov.app.util.timeToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.abs

class RestTimerProvider : RestProvider {
    companion object {
        @Volatile
        private var instance: RestTimerProvider? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RestTimerProvider().also { instance = it }
        }
    }

    private val _restTimer = MutableStateFlow(Rest())
    val restTimer: StateFlow<Rest>
        get() = _restTimer

    private val _restState = MutableStateFlow(RestState.Default)
    val restState: StateFlow<RestState>
        get() = _restState


    private lateinit var timer: CountDownTimer
    private var remainingTime: Long = 0
    private lateinit var restTimerStart: LocalDateTime
    private lateinit var restTimerEnd: LocalDateTime

    override suspend fun startRest(duration: Long, elapsedTime: Long) {
        _restState.emit(RestState.Active)
        restTimerStart = LocalDateTime.now()
        Log.d("Start of rest at start rest", restTimerStart.toString())
        restTimerEnd = restTimerStart.plusSeconds(duration / 1000)
        Log.d("End  of rest at start rest", duration.toString())
        Log.d("End  of rest at start rest", restTimerEnd.toString())

        Log.d("rest duration", duration.timeToString())

        if (remainingTime == 0L) {
            _restTimer.value.fullRest = duration.timeToString()
        }
        remainingTime = duration - abs(elapsedTime)
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(p0: Long) {
                CoroutineScope(Dispatchers.Main).launch {
                    remainingTime = p0
                    _restTimer.emit(
                        Rest(
                            elapsedTime = p0.timeToString(),
                            fullRest = _restTimer.value.fullRest
                        )
                    )
                }
            }

            override fun onFinish() {
                CoroutineScope(Dispatchers.Main).launch {
                    remainingTime = 0
                    _restState.emit(RestState.Finished)
                }
            }

        }.start()
    }

    override suspend fun stopRest() {
        timer.cancel()
        remainingTime = 0
        _restState.emit(RestState.Finished)
        _restState.value = RestState.Default
    }

    override suspend fun addTime(timeToAdd: Long) {
        timer.cancel()
        _restTimer.value.fullRest =
            (_restTimer.value.fullRest!!.parseTimeStringToLong() + timeToAdd).timeToString()
        startRest(remainingTime + timeToAdd)
        restTimerEnd.plusSeconds(timeToAdd)
    }

    override suspend fun removeTime(timeToRemove: Long) {
        if (remainingTime >= timeToRemove) {
            remainingTime -= timeToRemove
            timer.cancel()
            startRest(remainingTime)
        }
    }

    override fun isRestActive(): Boolean = restState.value == RestState.Active
    override fun getRestStartDate(): LocalDateTime = restTimerStart
    fun getEndOfRest() = restTimerEnd
    data class Rest(var elapsedTime: String? = null, var fullRest: String? = null)
}