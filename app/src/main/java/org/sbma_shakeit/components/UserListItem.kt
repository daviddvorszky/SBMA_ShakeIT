package org.sbma_shakeit.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sbma_shakeit.R
import org.sbma_shakeit.components.buttons.FriendsButton
import org.sbma_shakeit.components.buttons.FriendsButtonType
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.viewmodels.users.UserListItemViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserListItem(
    itemUser: User,
    userViewModel: UserViewModel,
    shake: Shake? = null
) {
    val userListItemViewModel = UserListItemViewModel(itemUser.username)
    val context = LocalContext.current
    val isCurrentUser by remember {
        mutableStateOf(userListItemViewModel.isCurrentUser)
    }
    val isUserFriend by remember {
        mutableStateOf(userListItemViewModel.isUserFriend)
    }
    val isRequestSent by remember {
        mutableStateOf(userListItemViewModel.isSentFriendReq)
    }

    var isAlertVisible by remember { mutableStateOf(false) }
    var alertTitle by remember { mutableStateOf("") }
    var alertText by remember { mutableStateOf("") }
    val alertDismiss = { isAlertVisible = false }
    var alertConfirm by remember { mutableStateOf({}) }

    val addFriendAction = {
        isAlertVisible = true
        alertTitle = context.getString(R.string.add_title)
        alertText = context.getString(R.string.add_text, itemUser.username)
        val confirm = {
            userViewModel.sendFriendRequest(itemUser.username)
            isRequestSent.value = true
            isAlertVisible = false
        }
        alertConfirm = confirm
    }

    val isFriendAction = {
        isAlertVisible = true
        alertTitle = context.getString(R.string.remove_title)
        alertText = context.getString(R.string.remove_text, itemUser.username)
        val confirm = {
            userViewModel.removeFromFriends(itemUser.username)
            isUserFriend.value = false
            isRequestSent.value = false
            isAlertVisible = false
        }
        alertConfirm = confirm
    }

    val cancelRequestAction = {
        isAlertVisible = true
        alertTitle = context.getString(R.string.cancel_title)
        alertText = context.getString(R.string.cancel_text, itemUser.username)
        val confirm = {
            userViewModel.removeFromFriends(itemUser.username)
            isRequestSent.value = false
            isAlertVisible = false
        }
        alertConfirm = confirm
    }

    // User list item card
    Card(
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isAlertVisible) {
                MyAlert(
                    title = alertTitle, text = alertText,
                    confirmAction = alertConfirm, dismissAction = alertDismiss
                )
            }

            // Users name and score/time
            Column {
                val username = if (isCurrentUser.value) "You" else itemUser.username
                Text(text = username, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(3.dp))
                if (shake != null) {
                    if (shake.type == Shake.TYPE_LONG) {
                        Text(text = "Best score: ${(shake.duration / 1000)} sec")
                    } else {
                        Text(text = "Best score: ${shake.score} points")
                    }
                }
            }

            // Display friends button
            if (!isCurrentUser.value) {
                if (isUserFriend.value) {
                    FriendsButton(
                        type = FriendsButtonType.FRIEND,
                        onClickAction = isFriendAction
                    )
                } else if (isRequestSent.value) {
                    FriendsButton(
                        type = FriendsButtonType.CANCEL,
                        onClickAction = cancelRequestAction
                    )
                } else {
                    FriendsButton(
                        type = FriendsButtonType.ADD,
                        onClickAction = addFriendAction
                    )
                }
            }
        }
    }
}

