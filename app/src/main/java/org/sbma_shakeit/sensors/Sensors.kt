package org.sbma_shakeit.sensors

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class AccelerometerSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_LINEAR_ACCELERATION
)