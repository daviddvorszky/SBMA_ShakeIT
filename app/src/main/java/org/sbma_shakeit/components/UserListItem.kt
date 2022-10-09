package org.sbma_shakeit.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.sbma_shakeit.R
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.viewmodels.UserListItemViewModel
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun UserListItem(itemUser: User, userViewModel: UserViewModel) {

    val vm = UserListItemViewModel(itemUser.username)
    val isCurrentUser by remember { mutableStateOf(vm.isCurrentUser) }
    val isUserFriend by remember { mutableStateOf(vm.isUserFriend) }
    val isRequestSent by remember { mutableStateOf(vm.isSentFriendReq) }

    var isAlertVisible by remember { mutableStateOf(false) }
    var alertTitle by remember { mutableStateOf("") }
    var alertText by remember { mutableStateOf("") }
    val alertDismiss = { isAlertVisible = false }
    var alertConfirm by remember { mutableStateOf({}) }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isAlertVisible) MyAlert(title = alertTitle, text = alertText,
            confirmAction = alertConfirm, dismissAction = alertDismiss)

        Text(itemUser.username)
        // TODO: Add users shake scores
        Column {
//            Text("Long ${itemUser.longShake.time}")
//            Text("Quick ${itemUser.quickShake.score}")
//            Text("Violent ${itemUser.violentShake.score}")
        }

        if (isCurrentUser.value) {
            Text("YOU")
        } else {
            if (isUserFriend.value) {
                Log.d("HERE", "HERE")
                Button(onClick = {
                    isAlertVisible = true
                    alertTitle = "Remove from friends"
                    alertText = "Do you want to remove this user from friends?"
                    val confirm = {
                        userViewModel.removeFromFriends(itemUser.username)
//                            vm.isUserFriend.value = false
                        isUserFriend.value = false
                        isRequestSent.value = false
                        isAlertVisible = false
                    }
                    alertConfirm = confirm
/////////////////////////////////////

                }) {
                    Row {
                        Text("Friend")
                        Icon(Icons.Filled.Done, contentDescription = "Is friend")
                    }
                }
            } else if (isRequestSent.value) {
                Button(onClick = {
                    isAlertVisible = true
                    alertTitle = "Cancel friend request"
                    alertText = "Do you want to cancel this friend request?"
                    val confirm = {
                        userViewModel.removeFromFriends(itemUser.username)
                        isRequestSent.value = false
                        isAlertVisible = false
                    }
                    alertConfirm = confirm
                }) {
                    Row {
                        Text("Cancel")
                        Icon(
                            painterResource(R.drawable.remove_request),
                            contentDescription = "Cancel friend request"
                        )
                    }
                }
            } else {
                IconButton(onClick = {
                    isAlertVisible = true
                    alertTitle = "Add to friends"
                    alertText = "Do you want to send a friend request to this user?"
                    val confirm = {
                        userViewModel.sendFriendRequest(itemUser.username)
                        isRequestSent.value = true
                        isAlertVisible = false
                    }
                    alertConfirm = confirm
                }) {
                    Icon(
                        painterResource(R.drawable.friend_add),
                        contentDescription = "Send friend request"
                    )
                }
            }
        }
    }
}

