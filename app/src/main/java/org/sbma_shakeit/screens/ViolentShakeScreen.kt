package org.sbma_shakeit.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sbma_shakeit.viewmodels.ViolentShakeViewModel

@Composable
fun ViolentShakeScreen(
    navController: NavController,
    viewModel: ViolentShakeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(viewModel.shouldShowCamera.value){
            CameraView(
                viewModel.outputDirectory,
                viewModel.cameraExecutor,
                viewModel::handleImageCapture,
                onError = { Log.e("ViolentShakeScreen", "View error:", it)}
            )
        }else {
            Text(
                "Violent Shake",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(105, 12, 204))
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                color = Color.White,
            )
            Text("Intensity: ${viewModel.shakeIntensity}")
            Text("Max Intensity: ${viewModel.maxShakeIntensity}")
            Button(
                onClick = {
                    if (viewModel.isSensorRunning) {
                        viewModel.shakeSensor.stopListening()
                    } else {
                        viewModel.shakeSensor.startListening()
                        viewModel.isSensorRunning = true
                        viewModel.isShaking = true
                    }
                },
            ) {
                Text(if (viewModel.isSensorRunning) "Stop" else "Start")
            }
            if (viewModel.shouldShowPhoto.value) {
                Image(
                    painter = rememberImagePainter(viewModel.photoUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            viewModel.shouldShowCamera.value = true
                        }
                )
            }else{
                Button(onClick = {
                    viewModel.shouldShowCamera.value = true
                }) {
                    Text("Take photo")
                }
            }
            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.saveShake()
                    }
                },
                enabled = viewModel.shakeExists && !viewModel.isSensorRunning
            ) {
                Text("Save shake")
            }
        }
    }
}