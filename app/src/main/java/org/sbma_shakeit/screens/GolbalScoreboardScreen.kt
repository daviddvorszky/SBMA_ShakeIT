package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.components.UserList
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun GlobalScoreboardScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    Column(Modifier.fillMaxSize()) {
        Text("Scoreboard global")
        UserList(viewModel = vm)
    }
}