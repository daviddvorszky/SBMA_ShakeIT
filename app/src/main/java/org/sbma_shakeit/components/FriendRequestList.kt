package org.sbma_shakeit.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun FriendRequestList(vm: UserViewModel = viewModel()) {
    val requests = vm.friendRequests

    Column(Modifier.padding(10.dp)) {
        Text("Received friend requests (${requests.size})")

        LazyColumn {
            items(requests/*, key = { it.sender }*/) { req ->
                Row(
                    Modifier.border(BorderStroke(2.dp, Color.Black)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(req.sender)
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = {
                        vm.acceptFriendRequest(req.receiver, req.sender)
                    }) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Accept friend request",
                            tint = Color.Green
                        )
                    }
                    IconButton(onClick = {
                        vm.removeFriendRequest(req.receiver, req.sender)
                    }) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Accept friend request",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}