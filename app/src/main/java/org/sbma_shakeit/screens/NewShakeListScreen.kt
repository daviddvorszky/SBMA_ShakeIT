package org.sbma_shakeit.screens

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.sbma_shakeit.MainActivity
import org.sbma_shakeit.R
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.HistoryViewModel
import org.sbma_shakeit.viewmodels.LocationViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun NewShakeListScreen(
    navController: NavController
) {

    //MAP VARIABLES
    val locationViewModel = LocationViewModel(application = Application(), Activity(), MainActivity.lm)
    val map = composeMap()

    Column(Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(5.dp))
        Card(
            elevation = 10.dp,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text("New Shake:", modifier = Modifier.padding(5.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .border(3.dp, MaterialTheme.colors.primary)
                        .background(MaterialTheme.colors.background),
                    horizontalArrangement = Arrangement.SpaceEvenly

                ) {
                    Button(modifier = Modifier.padding(5.dp), onClick = {
                        navController.navigate(Screen.QuickShake.route)
                    }) {
                        Text("Quick")
                    }
                    Button(modifier = Modifier.padding(5.dp), onClick = {
                        navController.navigate(Screen.LongShake.route)
                    }) {
                        Text("Long")
                    }
                    Button(modifier = Modifier.padding(5.dp), onClick = {
                        navController.navigate(Screen.ViolentShake.route)
                    }) {
                        Text("Violent")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Card(
            elevation = 10.dp,
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(10.dp)
        ) {
            ShowMap(locationViewModel = locationViewModel, navController, map)
        }
    }
}


@Composable
private fun composeMap(): MapView {
    val context = LocalContext.current
    val mapView = remember{
        MapView(context).apply { id = R.id.map }
    }
    return mapView
}

@Composable
private fun ShowMap(locationViewModel: LocationViewModel, navController: NavController, map: MapView){
    var mapInizialized by remember(map){ mutableStateOf(false) }
    val marker = Marker(map)
    val currentGeoPoint = locationViewModel.currentGeoPoint.observeAsState()
    val vmh = HistoryViewModel()
    val sp = ShakeProvider()
    val vm: UserViewModel = viewModel()
    val friendList = vm.friends

    val geoPoints = mutableListOf<GeoPoint>()
    //replace with firebase data
    geoPoints += GeoPoint(59.0, 25.1)
    geoPoints += GeoPoint(60.0, 25.2)
    geoPoints += GeoPoint(60.0, 25.3)


    if (!mapInizialized){
        map.setTileSource(TileSourceFactory.MAPNIK)     //Set the Tiles source
        map.setMultiTouchControls(true)                 //Ability to zoom with 2 fingers
        map.controller.setZoom(9.0)                     //Set the default zoom
        map.controller.setCenter(GeoPoint(60.0, 25.0)) //set the center of the map initialization

        mapInizialized = true
    }

    var shakesFriend  = locationViewModel.shakesFriend.observeAsState()
    AndroidView({map}){
        currentGeoPoint ?: return@AndroidView
        it.controller.setCenter(currentGeoPoint.value)


        friendList.forEach{                                         //for each friend of yours
            val friendUsername = it.username
            locationViewModel.getFriendsShakes(it)

            shakesFriend.value?.forEach{                                   //for each shake of it
                val marker = Marker(map)

                marker.position = GeoPoint(it.latitude.toDouble(), it.longitude.toDouble())
                marker.title =
                    "Friend: " + friendUsername + "-> Duration: " + it.duration + " Score: " + it.score
                map.overlays.add(marker)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
        }
        map.invalidate()
    }
}

