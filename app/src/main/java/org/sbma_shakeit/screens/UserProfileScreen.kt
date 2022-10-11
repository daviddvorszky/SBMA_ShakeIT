package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.components.FriendRequestList
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    val user = vm.getCurrentUser().observeAsState()
    val userData = user.value ?: return
//    val date = userData.quickShake.created
//    val f = SimpleDateFormat("dd.MM.yyyy").format(date)

    Column {
        // TODO: Profile data
        Text("User is ${userData.username}")
//        Text("date $f")
//        Text("Long shake time ${userData.longShake.time}s")
//        Text("Violent shake score ${userData.violentShake.score}")
//        Text("Quick shake score ${userData.quickShake.score}")
        FriendRequestList()
    }
}