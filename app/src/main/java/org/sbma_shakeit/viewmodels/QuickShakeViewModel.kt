package org.sbma_shakeit.viewmodels

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.MeasurableSensor
import java.io.File
import kotlin.math.sqrt

class QuickShakeViewModel(
    application: Application,
    database: ShakeItDB,
    fusedLocationClient: FusedLocationProviderClient,
    outputDirectory: File,
    var shakeSensor: MeasurableSensor,
): ShakeViewModel(application, database, fusedLocationClient, outputDirectory) {
    private val n = 60
    private var lastRecords = FloatArray(n)
    private var idx: Int = 0
    private var sum: Float = 0.0f

    val mainHandler = Handler(Looper.getMainLooper())

    init {
        shakeType = Shake.TYPE_QUICK
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

        timer.setStopCallback {
            shakeSensor.stopListening()
            stopScoring()
            shakeExists = true
        }

        shakeSensor.setOnStopListeningCallback {
            Log.d("SHAKE", "Quick shake stopped, score: $score")
            isSensorRunning = false
            isShaking = false
            shakeExists = true
        }
    }

    fun startScoring(){
        score = 0f
        mainHandler.post(updateScore)
        sum = 0f
        basicShake = false
        violentShake = false
    }

    private fun stopScoring(){
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