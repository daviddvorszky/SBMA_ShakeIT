package org.sbma_shakeit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun UserList(viewModel: UserViewModel) {

    val allUsers = viewModel.allUsers

    LazyColumn {
        items(allUsers) { user ->
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val isUser by remember {
                    mutableStateOf(viewModel.isUserFriend(user.username))
                }
                Text(user.username)
                if (user.username != viewModel.user.value?.username) {
                    if (isUser) {
                        Button(onClick = {
                            viewModel.removeFromFriends(user.username)
                        }) {
                            Text("Remove from friends")
                        }
                    } else {
                        Button(onClick = {
                            viewModel.sendFriendRequest(user.username)
                        }) {
                            Text("Add to friends")
                        }
                    }
                }
            }
        }
    }
}