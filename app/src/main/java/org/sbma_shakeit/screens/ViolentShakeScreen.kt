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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sbma_shakeit.R
import org.sbma_shakeit.viewmodels.ViolentShakeViewModel

@Composable
fun ViolentShakeScreen(
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
                stringResource(R.string.violent_shake),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color(105, 12, 204))
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                color = Color.White,
            )
            Text("${stringResource(R.string.intensity)}: ${viewModel.shakeIntensity}")
            Text("${stringResource(R.string.max_intensity)}: ${viewModel.maxShakeIntensity}")
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
                Text(if (viewModel.isSensorRunning) stringResource(R.string.stop) else stringResource(R.string.start))
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
                    Text(stringResource(R.string.take_photo))
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
                Text(stringResource(R.string.save_shake))
            }
        }
    }
}