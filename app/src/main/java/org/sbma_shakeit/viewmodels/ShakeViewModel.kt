package org.sbma_shakeit.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.*
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.data.web.UserProvider
import org.sbma_shakeit.tools.Stopper
import org.sbma_shakeit.tools.Timer
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class ShakeViewModel(
    private val database: ShakeItDB,
    var outputDirectory: File,
    fusedLocationClient: FusedLocationProviderClient,
    application: Application

): AndroidViewModel(application) {
    var cameraExecutor: ExecutorService

    var shouldShowCamera = mutableStateOf(false)

    lateinit var photoUri: Uri
    var shouldShowPhoto = mutableStateOf(false)

    protected val basicThreshold = 1f
    protected val violentThreshold = 4f

    protected var shakeType = -1

    protected var startTime = 0L

    private val shakeProvider = ShakeProvider()

    var latitude by mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)

    val stopper by mutableStateOf(Stopper())
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

        cameraExecutor = Executors.newSingleThreadExecutor()

        val locationCallback = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                val location = p0.lastLocation
                latitude = location?.latitude ?: latitude
                longitude = location?.longitude ?: longitude
            }
        }
        val locationRequest = LocationRequest
            .create()
            .setInterval(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                application.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("ShakeLocation", "FINE LOCATION access NOT granted")
        }else{
            Log.i("ShakeLocation", "FINE LOCATION access granted")
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    suspend fun saveShake(){
        // TODO: Image path should come from Firestore database
        val imagePath = ""

        val userProvider = UserProvider()
        val username = userProvider.getCurrentUser().username
        val duration = if(shakeType == Shake.TYPE_QUICK) 60_000 else stopper.timeMillis

        val shake = Shake("", shakeType, score, duration, username, imagePath, longitude.toFloat(), latitude.toFloat())
        Log.d("SaveShake", shake.toString())

        val image = if(this::photoUri.isInitialized) File(photoUri.path!!) else null

        shakeProvider.saveShake(shake, database, image)
    }

    fun handleImageCapture(uri: Uri){
        Log.i("ShakeViewModel", "Image captured: $uri")
        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true
    }
}