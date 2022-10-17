package org.sbma_shakeit.viewmodels

import android.app.Application
import android.location.Location
import android.location.LocationListener
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.web.ShakeProvider

class LocationViewModel(application: Application): AndroidViewModel(application), LocationListener {
    val sp = ShakeProvider()

    var currentGeoPoint = MutableLiveData(GeoPoint(60.224086, 24.758160))
    var shakesFriend = MutableLiveData(listOf<Shake>())

    fun getFriendsShakes(user: User){
        viewModelScope.launch {
            val awt = async { shakesFriend.value = sp.getShakesOfUser(user.username) }
            awt.await()
        }
    }

    override fun onLocationChanged(p0: Location) {
        currentGeoPoint.value?.latitude = p0.latitude
        currentGeoPoint.value?.longitude = p0.longitude
    }
}