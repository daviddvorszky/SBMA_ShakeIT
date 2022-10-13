package org.sbma_shakeit.viewmodels

import android.app.Activity
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
    activity: Activity,
    database: ShakeItDB,
    var shakeSensor: MeasurableSensor,
): ShakeViewModel(activity, database) {
    private val n = 20
    private val lastRecords = FloatArray(n)
    private var idx: Int = 0
    private var sum: Float = 0.0f

    init {
        shakeType = Shake.TYPE_LONG
        shakeSensor.setOnSensorValuesChangedListener { values ->
            val x = values[0]
            val y = values[1]
            val z = values[2]

            // Use average of the last n measurements to filter out errors/inaccuracies
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
                timer.start()
            }

            // Stop measuring when the shake intensity falls below the basic threshold
            if(isShaking && !basicShake){
                isShaking = false
                shakeSensor.stopListening()
            }
        }

        shakeSensor.setOnStopListeningCallback {
            Log.d("SHAKE", "Long shake stopped: ${timer.timeMillis/1000} sec")
            isSensorRunning = false
            timer.pause()
            shakeExists = true
        }
    }
}