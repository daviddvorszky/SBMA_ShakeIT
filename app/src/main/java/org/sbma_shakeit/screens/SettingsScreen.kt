package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.MainActivity.Companion.isDarkMode
import org.sbma_shakeit.components.MyAlert
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun SettingsScreen(
    navController: NavController
) {
    val userViewModel: UserViewModel = viewModel()
    var showAlert by remember { mutableStateOf(false) }
    Column() {
        if (showAlert) {
            MyAlert(
                title = "Delete account",
                text = "Do you want to remove this account from the app?",
                confirmAction = {
                    userViewModel.removeUser()
                    navController.navigate(Screen.Register.route)
                },
                dismissAction = { showAlert = false }
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Dark Mode")
            Switch(checked = isDarkMode.value,
                onCheckedChange = { isDarkMode.value = it} )
        }
        Button(onClick = { showAlert = true }) {
            Text("DELETE ACCOUNT")
        }
    }
}