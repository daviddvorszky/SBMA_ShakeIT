package org.sbma_shakeit.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.sbma_shakeit.sensors.MeasurableSensor
import kotlin.math.sqrt

class ViolentShakeViewModel(
    shakeSensor: MeasurableSensor
): ViewModel() {

    var shakeIntensity by mutableStateOf(0.0f)
    var maxShakeIntensity by mutableStateOf(0.0f)

    init {
        shakeSensor.startListening()
        shakeSensor.setOnSensorValuesChangedListener { values ->
            val x = values[0]
            val y = values[1]
            val z = values[2]

            shakeIntensity = sqrt(x*x + y*y + z*z)
            if(shakeIntensity > maxShakeIntensity)
                maxShakeIntensity = shakeIntensity
        }
    }
}