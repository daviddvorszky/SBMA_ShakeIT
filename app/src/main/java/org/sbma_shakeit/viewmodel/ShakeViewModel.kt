package org.sbma_shakeit.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.sbma_shakeit.sensors.MeasurableSensor
import kotlin.math.sqrt

class ShakeViewModel(
    shakeSensor: MeasurableSensor
): ViewModel() {
    private val n = 20
    private val lastRecords = FloatArray(n)
    private var idx: Int = 0
    private var sum: Float = 0.0f
    private val basicThreshold = 1f
    private val violentThreshold = 4f
    private var isShaking = false

    var shakeIntensity by mutableStateOf(0.0f)
    var basicShake by mutableStateOf(false)
    var violentShake by mutableStateOf(false)

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

            if(!isShaking && basicShake){
                isShaking = true
            }
            if(isShaking && !basicShake){
                isShaking = false
                shakeSensor.stopListening()
            }
        }
    }
}