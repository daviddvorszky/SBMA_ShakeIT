package org.sbma_shakeit.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import org.sbma_shakeit.R
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
    @SuppressLint("StaticFieldLeak") private val activity: Activity,
    private val database: ShakeItDB,

): ViewModel() {

    lateinit var outputDirectory: File
    lateinit var cameraExecutor: ExecutorService

    var shouldShowCamera = mutableStateOf(false)

    lateinit var photoUri: Uri
    var shouldShowPhoto = mutableStateOf(false)

    protected val basicThreshold = 1f
    protected val violentThreshold = 4f

    protected var shakeType = -1

    protected var startTime = 0L
    protected var currentTime = 0L

    private val shakeProvider = ShakeProvider()

    private var fusedLocationClient: FusedLocationProviderClient
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

        outputDirectory = getOutputDirectory_()
        cameraExecutor = Executors.newSingleThreadExecutor()

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
            }
        }
        val locationRequest = LocationRequest
            .create()
            .setInterval(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    suspend fun saveShake(){
        // TODO: Image path should come from Firestore database
        val imagePath = ""

        val userProvider = UserProvider()
        val username = userProvider.getCurrentUser().username
        val duration = if(shakeType == Shake.TYPE_QUICK) 60_000 else stopper.timeMillis

        val shake = Shake("", shakeType, score, duration, username, imagePath, longitude.toFloat(), latitude.toFloat())
        Log.d("SaveShake", "${shake.toString()}")

        val image = if(this::photoUri.isInitialized) File(photoUri.path) else null

        shakeProvider.saveShake(shake, database, image)
    }

    fun handleImageCapture(uri: Uri){
        Log.i("ShakeViewModel", "Image captured: $uri")
        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true
    }

    private fun getOutputDirectory_(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
            File(it, activity.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else activity.filesDir
    }
}