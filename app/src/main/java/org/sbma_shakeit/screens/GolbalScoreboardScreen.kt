package org.sbma_shakeit.screens

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.sbma_shakeit.components.UserList
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun GlobalScoreboardScreen(
    navController: NavController,
    vm: UserViewModel = UserViewModel(LocalContext.current.applicationContext as Application)
) {
    Column(Modifier.fillMaxSize()) {
        Text("Scoreboard global")
        UserList(viewModel = vm)
    }
}