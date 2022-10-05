package org.sbma_shakeit.sensors

import android.app.Application

object SensorModule {

    private var shakeSensor: MeasurableSensor? = null

    fun provideShakeSensor(app: Application): MeasurableSensor {
        if(shakeSensor == null){
            shakeSensor = AccelerometerSensor(app)
        }
        return shakeSensor!!
    }
}