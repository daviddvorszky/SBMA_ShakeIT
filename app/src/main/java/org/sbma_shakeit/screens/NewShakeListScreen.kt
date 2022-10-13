package org.sbma_shakeit.screens

import android.app.Activity
import android.app.Application
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
import androidx.navigation.NavController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.sbma_shakeit.MainActivity
import org.sbma_shakeit.R
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.LocationViewModel

@Composable
fun NewShakeListScreen(
    navController: NavController
) {

    //MAP VARIABLES
    val locationViewModel = LocationViewModel(application = Application(), Activity(), MainActivity.lm)

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
            ShowMap(locationViewModel = locationViewModel, navController)
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
private fun ShowMap(locationViewModel: LocationViewModel, navController: NavController){
    val map = composeMap()
    var mapInizialized by remember(map){ mutableStateOf(false) }
    val marker = Marker(map)
    val currentGeoPoint = locationViewModel.currentGeoPoint.observeAsState()


    if (!mapInizialized){
        map.setTileSource(TileSourceFactory.MAPNIK)     //Set the Tiles source
        map.setMultiTouchControls(true)                 //Ability to zoom with 2 fingers
        map.controller.setZoom(9.0)                     //Set the default zoom
        map.controller.setCenter(GeoPoint(60.0, 25.0)) //set the center of the map initialization

        mapInizialized = true
    }
    AndroidView({map}){
        currentGeoPoint ?: return@AndroidView
        it.controller.setCenter(currentGeoPoint.value)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.position = currentGeoPoint.value
        marker.closeInfoWindow()
        marker.title = "You are here! latitude: "+currentGeoPoint.value?.latitude+" longitude: "+currentGeoPoint.value?.longitude
        map.overlays.add(marker)
        map.invalidate()
    }
}

