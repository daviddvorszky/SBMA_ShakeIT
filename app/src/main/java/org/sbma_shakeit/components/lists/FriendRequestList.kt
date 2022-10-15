package org.sbma_shakeit.components.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun FriendRequestList(userViewModel: UserViewModel = viewModel()) {
    val requests = userViewModel.friendRequests

    LazyColumn {
        items(requests) { request ->
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(request.sender)
                Row {
                    Button(
                        onClick = {
                            userViewModel.acceptFriendRequest(request.receiver, request.sender)
                        },
                        colors = ButtonDefaults
                            .buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Accept friend request",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            userViewModel.removeFriendRequest(request.receiver, request.sender)
                        },
                        colors = ButtonDefaults
                            .buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Deny friend request",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}