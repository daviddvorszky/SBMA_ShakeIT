package org.sbma_shakeit.screens

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.MainActivity
import org.sbma_shakeit.viewmodels.LocationViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel
import org.sbma_shakeit.R

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
}

//        Text("date $f")
//        Text("Long shake time ${userData.longShake.time}s")
//        Text("Violent shake score ${userData.violentShake.score}")
//        Text("Quick shake score ${userData.quickShake.score}")
//            FriendRequestList()