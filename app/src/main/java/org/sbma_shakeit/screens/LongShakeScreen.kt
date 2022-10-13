package org.sbma_shakeit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sbma_shakeit.viewmodels.LongShakeViewModel

@Composable
fun LongShakeScreen(
    navController: NavController,
    viewModel: LongShakeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            "Long Shake",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
                .background(Color(77, 150, 232))
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            color = Color.White,
        )
        Text(
            text = viewModel.timer.formattedTime
        )
        Button(
            onClick = {
                viewModel.shakeSensor.startListening()
                viewModel.isSensorRunning = true
                viewModel.timer.reset()
            },
            enabled = !viewModel.isSensorRunning
        ) {
            Text(if(viewModel.shakeExists) "Restart" else "Start")
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