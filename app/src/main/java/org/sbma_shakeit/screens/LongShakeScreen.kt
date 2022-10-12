package org.sbma_shakeit.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.sbma_shakeit.viewmodels.LongShakeViewModel

@Composable
fun LongShakeScreen(
    navController: NavController,
    viewModel: LongShakeViewModel
) {
    //Text("Long Shake Screen")
    Text("Location: [${viewModel.latitude}, ${viewModel.longitude}]")
}