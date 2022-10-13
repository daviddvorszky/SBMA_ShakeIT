package org.sbma_shakeit.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.data.web.UserProvider
import org.sbma_shakeit.tools.Timer

open class ShakeViewModel(
    @SuppressLint("StaticFieldLeak") private val activity: Activity,
    private val database: ShakeItDB,
): ViewModel() {
    protected val basicThreshold = 1f
    protected val violentThreshold = 4f

    protected var shakeType = -1

    protected var startTime = 0L
    protected var currentTime = 0L

    private val shakeProvider = ShakeProvider()

    private var fusedLocationClient: FusedLocationProviderClient
    var latitude by mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)

    val timer by mutableStateOf(Timer())

    var isSensorRunning by mutableStateOf(false)
    var isShaking by mutableStateOf(false)
    var shakeIntensity by mutableStateOf(0.0f)
    var basicShake by mutableStateOf(false)
    var violentShake by mutableStateOf(false)
    var score by mutableStateOf(0f)
    var shakeExists by mutableStateOf(false)

    init {
        Log.d("pengb", "Shake View Model init")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        if(ActivityCompat.checkSelfPermission(activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.i("ShakeLocation", "FINE LOCATION access granted")
        }else{
            Log.d("pengb", "FINE LOCATION access NOT granted")
        }
        val locationCallback = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                val location = p0.lastLocation
                latitude = location?.latitude ?: latitude
                longitude = location?.longitude ?: longitude

                Log.d("pengb", "${latitude} ${longitude}")
            }
        }
        val locationRequest = LocationRequest
            .create()
            .setInterval(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    suspend fun saveShake(){
        // TODO: Shake id should come from Firestore database
        val shakeId = (Math.random()*100_000).toString()

        // TODO: Image path should come from Firestore database
        val imagePath = ""

        val userProvider = UserProvider()
        val username = userProvider.getCurrentUser().username
        val shake = Shake("", shakeType, score, timer.timeMillis, username, imagePath, longitude.toFloat(), latitude.toFloat())
        Log.d("SaveShake", "${shake.toString()}")

        shakeProvider.saveShake(shake, database)
    }
}