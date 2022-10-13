package org.sbma_shakeit.viewmodels

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.osmdroid.util.GeoPoint
import java.util.*

class LocationViewModel(application: Application, activity: Activity, lm: LocationManager): AndroidViewModel(application), LocationListener {
    private val app = application
    private val activity = activity
    private val lm = lm
    private var currentLocation: Location = Location(LocationManager.GPS_PROVIDER)
    private var startingPoint: Location = Location(LocationManager.GPS_PROVIDER)
    private var startingPointTime = Calendar.getInstance().timeInMillis
    private var endingPoint: Location = Location(LocationManager.GPS_PROVIDER)
    private var endingPointTime = Calendar.getInstance().timeInMillis

    //LIVEDATA TRACKING
    var speedLiveData = MutableLiveData<Float>(0f)
    var latitudeLiveData = MutableLiveData<Double>(0.0)
    var longitudeLiveData = MutableLiveData<Double>(0.0)
    var startingPointLiveData = MutableLiveData<Location>(Location(LocationManager.GPS_PROVIDER))
    var endingPointLiveData = MutableLiveData<Location>(Location(LocationManager.GPS_PROVIDER))
    var distanceFromStartingPoint = MutableLiveData<Float>(0f)
    var fixedDistanceLiveData = MutableLiveData(startingPointLiveData.value?.distanceTo(endingPointLiveData.value))
    //

    //LIVEDATA MAP
    var currentGeoPoint = MutableLiveData<GeoPoint>(GeoPoint(0.0,0.0))
    //

    fun hasPermission(): Boolean{                                                    //check permission to scan
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),0)
            return false
        }
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),0)
            return false
        }
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }

    fun getSpeed(){
        if (startingPoint.latitude != 0.0 && startingPoint.longitude != 0.0 && endingPoint.latitude != 0.0 && endingPoint.longitude != 0.0)
            speedLiveData.value = ((startingPoint.distanceTo(endingPoint)/1000.0)/((endingPointTime-startingPointTime)/1000.0/60.0/60.0)).toFloat()
        else
            speedLiveData.value = 0f
    }

    fun startLocationTracking() {
        //REQUEST BACKGROUND LOCATION PERMISSION
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),0)
        }
        //

        Log.d("DBG", "Location tracking has started!")
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),0)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),0)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),0)
            return
        }else{
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f, this)
        }
    }

    fun stopLocationTracking(){
        Log.d("DBG", "Location tracking has stopped!")
        lm.removeUpdates(this)
    }

    fun fixStartPoint(){
        startingPoint = currentLocation
        startingPointLiveData.value = currentLocation
        startingPointTime = Calendar.getInstance().timeInMillis
    }

    fun fixEndPoint(){
        endingPoint = currentLocation
        endingPointLiveData.value = currentLocation
        endingPointTime = Calendar.getInstance().timeInMillis
        fixedDistanceLiveData.value = startingPointLiveData.value?.distanceTo(endingPointLiveData.value)
        getSpeed()
    }

    fun getCurrentGeoPoint(): GeoPoint{
        return currentGeoPoint.value!!
    }

    override fun onLocationChanged(p0: Location) {
        Log.d("DBG", "Latitude: ${p0.latitude}, longitude: ${p0.longitude}, etc: $p0")
        //TRACKING
        latitudeLiveData.value = p0.latitude
        longitudeLiveData.value = p0.longitude
        //

        //MAP
        currentGeoPoint.value?.latitude = p0.latitude
        currentGeoPoint.value?.longitude = p0.longitude
        //

        if (startingPoint.latitude != 0.0 && startingPoint.longitude != 0.0)
            distanceFromStartingPoint.value = p0.distanceTo(startingPoint)

        currentLocation = p0
    }
}