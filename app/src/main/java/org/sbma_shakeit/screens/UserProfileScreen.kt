package org.sbma_shakeit.screens

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.sbma_shakeit.MainActivity
import org.sbma_shakeit.viewmodels.LocationViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel
import org.sbma_shakeit.R
import org.sbma_shakeit.viewmodels.HistoryViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    val user = vm.getCurrentUser().observeAsState()
    val userData = user.value ?: return
    val locationViewModel = LocationViewModel(application = Application(), Activity(), MainActivity.lm)
//    val date = userData.quickShake.created
//    val f = SimpleDateFormat("dd.MM.yyyy").format(date)

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
            val imageResulted = remember { mutableStateOf<Bitmap?>(null) }
            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()){ imageResulted.value = it }
            var userImage: ImageBitmap = ImageBitmap.imageResource(id = org.osmdroid.library.R.drawable.person)

            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(5.dp))
                IconButton(onClick = { launcher.launch() }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Localized description")
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    imageResulted.value?.let { image ->
                        userImage = image.asImageBitmap()
                    }

                    Image(
                        userImage,
                        "Downloaded image",
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth(0.5f)
                            .padding(5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ) {
                    Row(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Username", fontWeight = FontWeight.Bold)
                        Text(userData.username)
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ) {
                    Row(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Long shake time: ", fontWeight = FontWeight.Bold)
                        Text(userData.longShake.toString())
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ) {
                    Row(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Violent shake score: ", fontWeight = FontWeight.Bold)
                        Text(userData.violentShake.toString())
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ) {
                    Row(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Quick shake score: ", fontWeight = FontWeight.Bold)
                        Text(userData.quickShake.toString())
                    }
                }

                //Spacer(modifier = Modifier.height(10.dp))
                Card(
                    elevation = 5.dp,
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Column(Modifier.fillMaxWidth().background(MaterialTheme.colors.primaryVariant)) {
                        Text(text = "My shakes:", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(3.dp))
                        ShowMap(locationViewModel = locationViewModel, navController)
                    }
                }
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
    val vm = HistoryViewModel()

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

        vm.allShakes.forEach{
            var marker = Marker(map)
            marker.position = GeoPoint(it.latitude.toDouble(), it.longitude.toDouble())
            marker.title = "Duration: "+it.duration+"Score: "+it.score
            map.overlays.add(marker)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.closeInfoWindow()
        }

        map.invalidate()
    }
}


//        Text("date $f")
//        Text("Long shake time ${userData.longShake.time}s")
//        Text("Violent shake score ${userData.violentShake.score}")
//        Text("Quick shake score ${userData.quickShake.score}")
//            FriendRequestList()