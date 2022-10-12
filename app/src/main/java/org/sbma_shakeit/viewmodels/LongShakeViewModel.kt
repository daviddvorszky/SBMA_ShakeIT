package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.MeasurableSensor
import java.lang.System.currentTimeMillis
import kotlin.math.sqrt

class LongShakeViewModel(
    shakeSensor: MeasurableSensor,
    database: ShakeItDB
): ViewModel() {
    private val n = 20
    private val lastRecords = FloatArray(n)
    private var idx: Int = 0
    private var sum: Float = 0.0f
    private val basicThreshold = 1f
    private val violentThreshold = 4f
    private var isShaking = false
    private var startTime = 0L
    private var currentTime = 0L

    var shakeIntensity by mutableStateOf(0.0f)
    var basicShake by mutableStateOf(false)
    var violentShake by mutableStateOf(false)
    var timePassed by mutableStateOf(0L)

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

            // Start measuring when the shake intensity crosses the basic threshold
            if(!isShaking && basicShake){
                isShaking = true
                startTime = currentTimeMillis()
            }

            // Stop measuring when the shake intensity falls below the basic threshold
            if(isShaking && !basicShake){
                isShaking = false
                shakeSensor.stopListening()
            }

            if(isShaking) {
                currentTime = currentTimeMillis()
                timePassed = (currentTime - startTime) / 1000
            }
        }

        shakeSensor.setOnStopListeningCallback {
            Log.d("SHAKE", "Long shake stopped: $timePassed sec")
            GlobalScope.launch(Dispatchers.IO) {
                database.shakeDao().insert(
                    Shake(
                        (Math.random()*10000).toInt(),
                        Shake.TYPE_LONG,
                        0,
                        timePassed,
                        "testuser",
                        null,
                        0f, 0f
                    )
                )
            }
        }
    }
}