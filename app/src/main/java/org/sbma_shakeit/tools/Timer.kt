package org.sbma_shakeit.tools

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Timer {
    var formattedTime by mutableStateOf("60:00:000")
    private lateinit var callback: () -> Unit

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isActive = false

    private var timeMillis = 0L
    private var stopTimestamp = 0L

    fun setStopCallback(stopCallback: ()->Unit){
        callback = stopCallback
    }

    fun start(){
        if(isActive) return

        coroutineScope.launch {
            stopTimestamp = System.currentTimeMillis() + 60_000
            this@Timer.isActive = true
            while(this@Timer.isActive){
                delay(10L)
                timeMillis = stopTimestamp - System.currentTimeMillis()
                if(timeMillis <= 0){
                    timeMillis = 0
                    pause()
                    callback.invoke()
                }
                formattedTime = formatTime(timeMillis)
            }
        }
    }

    private fun pause(){
        isActive = false
    }

    fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        stopTimestamp = 0L
        formattedTime = "60:00:000"
        isActive = false
    }

    private fun formatTime(timeMillis: Long): String{
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss:SSS"
        )
        return localDateTime.format(formatter)
    }
}