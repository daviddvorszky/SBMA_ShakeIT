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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.R
import org.sbma_shakeit.viewmodels.SingleShakeViewModel

@Composable
fun ShakeScreen(vm: SingleShakeViewModel) {
    val types = arrayOf("Long", "Quick", "Violent", "${stringResource(R.string.null_value)}")
    val show = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("${stringResource(R.string.parent)}: ${vm.shake?.parent ?: "${stringResource(R.string.null_value)}"}")
        Text("${stringResource(R.string.type)}: ${types[vm.shake?.type ?: 3]}")
        Text("${stringResource(R.string.duration)}: ${vm.shake?.duration ?: "${stringResource(R.string.null_value)}"}")
        Text("${stringResource(R.string.score)}: ${vm.shake?.score ?: "${stringResource(R.string.null_value)}"}")
        Text("${stringResource(R.string.latitude)}: ${vm.shake?.latitude ?: "${stringResource(R.string.null_value)}"}")
        Text("${stringResource(R.string.longitude)}: ${vm.shake?.longitude ?: "${stringResource(R.string.null_value)}"}")

        Image(
            painter = rememberImagePainter(vm.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )

    }

}