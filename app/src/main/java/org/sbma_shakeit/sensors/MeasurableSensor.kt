package org.sbma_shakeit.sensors

abstract class MeasurableSensor(
    protected val sensorType: Int
) {
    protected var onSensorValueChanged: ((List<Float>) -> Unit)? = null

    abstract val doesSensorExists: Boolean

    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValueChanged = listener
    }
}