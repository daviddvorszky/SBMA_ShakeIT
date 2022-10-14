package org.sbma_shakeit.viewmodels

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.osmdroid.util.GeoPoint

class LocationViewModel(application: Application, activity: Activity, lm: LocationManager): AndroidViewModel(application), LocationListener {
    private val app = application
    private val activity = activity

    var currentGeoPoint = MutableLiveData<GeoPoint>(GeoPoint(60.224086, 24.758160))

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

    override fun onLocationChanged(p0: Location) {
        currentGeoPoint.value?.latitude = p0.latitude
        currentGeoPoint.value?.longitude = p0.longitude
    }
}