package org.sbma_shakeit.viewmodels

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModel
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.MeasurableSensor
import kotlin.math.sqrt

class QuickShakeViewModel(
    activity: Activity,
    database: ShakeItDB,
    shakeSensor: MeasurableSensor,
    username: String
): ShakeViewModel(activity, database, username) {
    private val n = 100
    private val lastRecords = FloatArray(n)
    private var idx: Int = 0
    private var sum: Float = 0.0f

    val mainHandler = Handler(Looper.getMainLooper())

    init {
        shakeSensor.setOnSensorValuesChangedListener { values ->
            val x = values[0]
            val y = values[1]
            val z = values[2]

            // Store last n measurements to calculate sum and average
            sum -= lastRecords[idx]
            lastRecords[idx] = sqrt(x*x + y*y + z*z)
            sum += lastRecords[idx]
            idx = (idx+1)%n

            shakeIntensity = sum/n

            basicShake = shakeIntensity > basicThreshold
            violentShake = shakeIntensity > violentThreshold
        }

        shakeSensor.setOnStopListeningCallback {
            Log.d("SHAKE", "Quick shake stopped, score: $score")
        }
    }

    fun startScoring(){
        mainHandler.post(updateScore)
    }

    fun stopScoring(){
        mainHandler.removeCallbacks(updateScore)
    }


    private val updateScore = object : Runnable {
        override fun run() {
            addScore()
            mainHandler.postDelayed(this, 1000)
        }
    }

    fun addScore(){
        if(violentShake){
            score += 3
        }else if(basicShake){
            score += 1
        }
    }
}