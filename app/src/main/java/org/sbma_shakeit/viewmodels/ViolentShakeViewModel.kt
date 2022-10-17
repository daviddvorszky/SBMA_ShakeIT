package org.sbma_shakeit.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.location.FusedLocationProviderClient
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.sensors.MeasurableSensor
import java.io.File
import kotlin.math.sqrt

class ViolentShakeViewModel(
    application: Application,
    database: ShakeItDB,
    fusedLocationClient: FusedLocationProviderClient,
    outputDirectory: File,
    var shakeSensor: MeasurableSensor,
): ShakeViewModel(application, database, fusedLocationClient, outputDirectory) {

    var maxShakeIntensity by mutableStateOf(0.0f)

    init {
        shakeType = Shake.TYPE_VIOLENT
        shakeSensor.setOnSensorValuesChangedListener { values ->
            val x = values[0]
            val y = values[1]
            val z = values[2]

            shakeIntensity = sqrt(x*x + y*y + z*z)
            if(shakeIntensity > maxShakeIntensity)
                maxShakeIntensity = shakeIntensity
        }
        shakeSensor.setOnStopListeningCallback {
            score = maxShakeIntensity
            Log.d("SHAKE", "Violent shake stopped, max intensity: $score")
            isSensorRunning = false
            isShaking = false
            shakeExists = true
        }
    }
}