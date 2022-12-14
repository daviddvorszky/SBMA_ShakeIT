package org.sbma_shakeit.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.sbma_shakeit.R
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.LocationViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun NewShakeListScreen(
    navController: NavController
) {

    //MAP VARIABLES
    val locationViewModel =
        LocationViewModel(application = Application())
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
                Text("${stringResource(R.string.new_shake)}:", modifier = Modifier.padding(5.dp))

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
                        Text(stringResource(R.string.quick))
                    }
                    Button(modifier = Modifier.padding(5.dp), onClick = {
                        navController.navigate(Screen.LongShake.route)
                    }) {
                        Text(stringResource(R.string.long_))
                    }
                    Button(modifier = Modifier.padding(5.dp), onClick = {
                        navController.navigate(Screen.ViolentShake.route)
                    }) {
                        Text(stringResource(R.string.violent))
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
            ShowMap(locationViewModel = locationViewModel, map)
        }
    }
}


@Composable
private fun composeMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply { id = R.id.map }
    }
    return mapView
}

@Composable
private fun ShowMap(locationViewModel: LocationViewModel, map: MapView) {
    var mapInitialized by remember(map){ mutableStateOf(false) }
    val currentGeoPoint = locationViewModel.currentGeoPoint.observeAsState()
    val vm: UserViewModel = viewModel()
    val friendList = vm.friends
    val context = LocalContext.current

    val geoPoints = mutableListOf<GeoPoint>()
    //replace with firebase data
    geoPoints += GeoPoint(59.0, 25.1)
    geoPoints += GeoPoint(60.0, 25.2)
    geoPoints += GeoPoint(60.0, 25.3)

    if (!mapInitialized) {
        map.setTileSource(TileSourceFactory.MAPNIK)     //Set the Tiles source
        map.setMultiTouchControls(true)                 //Ability to zoom with 2 fingers
        map.controller.setZoom(9.0)                     //Set the default zoom
        map.controller.setCenter(GeoPoint(60.0, 25.0)) //set the center of the map initialization

        mapInitialized = true
    }

    val shakesFriend = locationViewModel.shakesFriend.observeAsState()
    AndroidView({ map }) {
        it.controller.setCenter(currentGeoPoint.value)


        friendList.forEach { friend ->
            val friendUsername = friend.username
            locationViewModel.getFriendsShakes(friend)

            shakesFriend.value?.forEach { shake ->
                val marker = Marker(map)

                marker.position = GeoPoint(shake.latitude.toDouble(), shake.longitude.toDouble())
                marker.title =
                    "${context.getString(R.string.friend)}: " + friendUsername + "-> ${
                        context.getString(
                            R.string.duration
                        )
                    }: " + shake.duration + " ${context.getString(R.string.score)}: " + shake.score
                map.overlays.add(marker)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
        }
        map.invalidate()
    }
}

