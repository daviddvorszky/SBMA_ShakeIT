package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.data.User
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun GlobalScoreboardScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    Column {
        Text("Scoreboard global")
        LazyColumn {
            items(vm.allUsers.value as List<User>) { user ->
                Text(user.username)
            }
        }
    }
}