package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.sbma_shakeit.navigation.Screen

@Composable
fun NewShakeListScreen(
    navController: NavController
) {
    Column(Modifier.fillMaxSize()) {
        Text("New Shake List Screen")
        Button(onClick = {
            navController.navigate(Screen.QuickShake.route)
        }) {
            Text("Quick Shake")
        }
        Button(onClick = {
            navController.navigate(Screen.LongShake.route)
        }) {
            Text("Long Shake")
        }
        Button(onClick = {
            navController.navigate(Screen.ViolentShake.route)
        }) {
            Text("Violent Shake")
        }
    }
}