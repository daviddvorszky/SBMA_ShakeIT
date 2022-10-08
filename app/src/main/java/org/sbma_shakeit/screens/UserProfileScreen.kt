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
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

@Composable
fun UserProfileScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    val user = vm.user.observeAsState()
    val reqs = vm.friendRequests

    val userData = user.value ?: return
    val date = userData.quickShake.created
    val f = SimpleDateFormat("dd.MM.yyyy").format(date)

    Column {
        Text("User is ${userData.username}")
        Text("date $f")
        Text("Long shake time ${userData.longShake.time}s")
        Text("Violent shake score ${userData.violentShake.score}")
        Text("Quick shake score ${userData.quickShake.score}")
        FriendRequestList()
    }
}