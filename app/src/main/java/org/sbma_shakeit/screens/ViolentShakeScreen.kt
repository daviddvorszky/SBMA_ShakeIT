package org.sbma_shakeit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
                if(viewModel.isSensorRunning){
                    viewModel.shakeSensor.stopListening()
                }else{
                    viewModel.shakeSensor.startListening()
                    viewModel.isSensorRunning = true
                    viewModel.isShaking = true
                }
            },
        ) {
            Text(if(viewModel.isSensorRunning) "Stop" else "Start")
        }
        Text("Image placeholder")
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