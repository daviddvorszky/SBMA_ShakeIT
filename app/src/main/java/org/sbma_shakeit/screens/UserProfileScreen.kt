package org.sbma_shakeit.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.components.FriendRequestList
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    val user = vm.user.observeAsState()
    val reqs = vm.friendRequests
    val userData = user.value ?: return
    Column {
        Text("User is ${userData.username}")
        FriendRequestList()
    }
}