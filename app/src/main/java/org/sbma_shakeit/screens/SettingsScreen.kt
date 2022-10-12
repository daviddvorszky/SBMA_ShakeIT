package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.sbma_shakeit.MainActivity.Companion.isDarkMode

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Column() {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Dark Mode")
            Switch(checked = isDarkMode.value,
                onCheckedChange = { isDarkMode.value = it} )
        }

    }
}