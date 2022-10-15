package org.sbma_shakeit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.viewmodels.SingleShakeViewModel

@Composable
fun ShakeScreen(vm: SingleShakeViewModel) {
    val types = arrayOf("Long", "Quick", "Violent", "null")
    val show = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Parent: ${vm.shake?.parent ?: "null"}")
        Text("Type: ${types[vm.shake?.type ?: 3]}")
        Text("Duration: ${vm.shake?.duration ?: "null"}")
        Text("Score: ${vm.shake?.score ?: "null"}")
        Text("Latitude: ${vm.shake?.latitude ?: "null"}")
        Text("Longitude: ${vm.shake?.longitude ?: "null"}")

        Image(
            painter = rememberImagePainter(vm.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )

    }

}