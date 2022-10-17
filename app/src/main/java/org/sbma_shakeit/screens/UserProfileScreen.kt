package org.sbma_shakeit.screens

import android.app.Application
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.sbma_shakeit.R
import org.sbma_shakeit.viewmodels.HistoryViewModel
import org.sbma_shakeit.viewmodels.LocationViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserProfileScreen(
    vm: UserViewModel = viewModel()
) {
    val user = vm.getCurrentUser().observeAsState()
    val userData = user.value ?: return
    val locationViewModel = LocationViewModel(application = Application())
    val historyViewModel = HistoryViewModel()
    var maxLongShake = 0L
    var maxQuickShake = 0L
    var maxViolentShake = 0L

    historyViewModel.longShakes.forEach{
        if (it.duration > maxLongShake)
            maxLongShake = it.duration
    }
    historyViewModel.quickShakes.forEach{
        if (it.duration > maxQuickShake)
            maxQuickShake = it.duration
    }
    historyViewModel.violentShakes.forEach{
        if (it.duration > maxQuickShake)
            maxViolentShake = it.duration
    }
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
                        Text(text = stringResource(R.string.username), fontWeight = FontWeight.Bold)
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
                        Text(text = "${stringResource(R.string.long_shake_time)}: ", fontWeight = FontWeight.Bold)
                        Text(maxLongShake.toString())
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
                        Text(text = "${stringResource(R.string.violent_shake_score)}: ", fontWeight = FontWeight.Bold)
                        Text(maxViolentShake.toString())
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
                        Text(text = "${stringResource(R.string.quick_shake_score)}: ", fontWeight = FontWeight.Bold)
                        Text(maxQuickShake.toString())
                    }
                }

                //Spacer(modifier = Modifier.height(10.dp))
                Card(
                    elevation = 5.dp,
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(1.dp)
                    ) {
                        Text(text = "${stringResource(R.string.my_shakes)}:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(10.dp))
                        Spacer(modifier = Modifier.height(3.dp))
                        ShowMap(locationViewModel = locationViewModel)
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
private fun ShowMap(locationViewModel: LocationViewModel){
    val map = composeMap()
    val vm = HistoryViewModel()

    val currentGeoPoint = locationViewModel.currentGeoPoint.observeAsState()

    val context = LocalContext.current


        map.setTileSource(TileSourceFactory.MAPNIK)     //Set the Tiles source
        map.setMultiTouchControls(true)                 //Ability to zoom with 2 fingers
        map.controller.setZoom(9.0)                     //Set the default zoom
        map.controller.setCenter(GeoPoint(60.0, 25.0)) //set the center of the map initialization

    AndroidView({map}){
        it.controller.setCenter(currentGeoPoint.value)

        vm.allShakes.forEach{ shake ->
            val marker = Marker(map)
            marker.position = GeoPoint(shake.latitude.toDouble(), shake.longitude.toDouble())
            marker.title = "${context.getString(R.string.duration)}: "+shake.duration+"${context.getString(R.string.score)}: "+shake.score
            map.overlays.add(marker)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.closeInfoWindow()
        }

        map.invalidate()
    }
}