package org.sbma_shakeit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import org.sbma_shakeit.R
import org.sbma_shakeit.viewmodels.SingleShakeViewModel

@Composable
fun ShakeScreen(vm: SingleShakeViewModel) {
    val types = arrayOf("Long", "Quick", "Violent", stringResource(R.string.null_value))
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("${stringResource(R.string.parent)}: ${vm.shake.parent}")
        Text("${stringResource(R.string.type)}: ${types[vm.shake.type]}")
        Text("${stringResource(R.string.duration)}: ${vm.shake.duration}")
        Text("${stringResource(R.string.score)}: ${vm.shake.score}")
        Text("${stringResource(R.string.latitude)}: ${vm.shake.latitude}")
        Text("${stringResource(R.string.longitude)}: ${vm.shake.longitude}")

        Image(
            painter = rememberImagePainter(vm.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
        )

    }

}