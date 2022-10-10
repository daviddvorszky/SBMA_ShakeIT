package org.sbma_shakeit.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sbma_shakeit.components.buttons.FriendsButton
import org.sbma_shakeit.components.buttons.FriendsButtonType
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
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isAlertVisible) MyAlert(
            title = alertTitle, text = alertText,
            confirmAction = alertConfirm, dismissAction = alertDismiss
        )

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
                val onClick = {
                    isAlertVisible = true
                    alertTitle = "Remove from friends"
                    alertText = "Do you want to remove ${itemUser.username} from friends?"
                    val confirm = {
                        userViewModel.removeFromFriends(itemUser.username)
//                            vm.isUserFriend.value = false
                        isUserFriend.value = false
                        isRequestSent.value = false
                        isAlertVisible = false
                    }
                    alertConfirm = confirm
                }
                FriendsButton(type = FriendsButtonType.FRIEND, onClickAction = onClick)
            } else if (isRequestSent.value) {
                val onClick = {
                    isAlertVisible = true
                    alertTitle = "Cancel friend request"
                    alertText = "Do you want to cancel this friend request?"
                    val confirm = {
                        userViewModel.removeFromFriends(itemUser.username)
                        isRequestSent.value = false
                        isAlertVisible = false
                    }
                    alertConfirm = confirm
                }
                FriendsButton(type = FriendsButtonType.CANCEL, onClickAction = onClick)
            } else {
                val onClick = {
                    isAlertVisible = true
                    alertTitle = "Add to friends"
                    alertText = "Do you want to send a friend request to ${itemUser.username}?"
                    val confirm = {
                        userViewModel.sendFriendRequest(itemUser.username)
                        isRequestSent.value = true
                        isAlertVisible = false
                    }
                    alertConfirm = confirm
                }
                FriendsButton(type = FriendsButtonType.ADD, onClickAction = onClick)
            }
        }
    }
}

