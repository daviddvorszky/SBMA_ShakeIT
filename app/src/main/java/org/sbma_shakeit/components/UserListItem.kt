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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


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
                    userViewModel.removeFromFriends(itemUser.username)
//                            vm.isUserFriend.value = false
                    isUserFriend.value = false
                    isRequestSent.value = false
                }) {
                    Row {
                        Text("Friend")
                        Icon(Icons.Filled.Done, contentDescription = "Is friend")
                    }
                }
            }
            else if (isRequestSent.value) {
                Button(onClick = {
                    userViewModel.removeFromFriends(itemUser.username)
                    isRequestSent.value = false
                }) {
                    Row {
                        Text("Cancel")
                        Icon(
                            painterResource(R.drawable.remove_request),
                            contentDescription = "Cancel friend request")
                    }
                }
            }
            else {
                IconButton(onClick = {
                    userViewModel.sendFriendRequest(itemUser.username)
                    isRequestSent.value = true
                }) {
                    Icon(
                        painterResource(R.drawable.friend_add),
                        contentDescription = "Send friend request")
                }
            }
        }
    }
}
