package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun FriendListScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    // TODO: Get friends from room
    val friendList = vm.friends
    Column {
        Text("Friend List Screen")

        Text("Friends (${friendList.size})")
        LazyColumn {
            items(friendList) { friend ->
                Text(friend.username)
            }
        }
    }
}