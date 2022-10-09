package org.sbma_shakeit.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModel
import org.sbma_shakeit.sensors.MeasurableSensor
import kotlin.math.sqrt

class QuickShakeViewModel(
    shakeSensor: MeasurableSensor
): ViewModel() {
    private val n = 100
    private val lastRecords = FloatArray(n)
    private var idx: Int = 0
    private var sum: Float = 0.0f
    private val basicThreshold = 1f
    private val violentThreshold = 4f

    var shakeIntensity by mutableStateOf(0.0f)
    var basicShake by mutableStateOf(false)
    var violentShake by mutableStateOf(false)

    val mainHandler = Handler(Looper.getMainLooper())
    var score by mutableStateOf(0)

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