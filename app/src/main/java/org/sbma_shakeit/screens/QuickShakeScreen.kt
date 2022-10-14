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
import org.sbma_shakeit.viewmodels.QuickShakeViewModel

@Composable
fun QuickShakeScreen(
    navController: NavController,
    viewModel: QuickShakeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            "Quick Shake",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
                .background(Color(201, 52, 52))
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            color = Color.White,
        )
        Text(
            text = viewModel.timer.formattedTime
        )
        Text("Score: ${viewModel.score.toInt()}")
        Button(
            onClick = {
                viewModel.timer.reset()
                viewModel.shakeSensor.startListening()
                viewModel.isSensorRunning = true
                viewModel.isShaking = true
                viewModel.timer.start()
                viewModel.startScoring()
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