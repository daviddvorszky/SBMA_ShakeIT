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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.sbma_shakeit.R
import org.sbma_shakeit.data.User
import org.sbma_shakeit.viewmodels.UserListItemViewModel
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun UserListItem(itemUser: User, viewModel: UserViewModel) {
    val currentUser = viewModel.user.observeAsState()
    val vm = UserListItemViewModel(currentUser.value?.username ?: "", itemUser.username)
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val _isUserFriend = vm.isUserFriend.observeAsState()

        val isRequestSent by remember {
            mutableStateOf(vm.isSentFriendReq)
        }

        Text(itemUser.username)
        Column {
            Text("Long ${itemUser.longShake.time}")
            Text("Quick ${itemUser.quickShake.score}")
            Text("Violent ${itemUser.violentShake.score}")
        }
        //------------------------------------------------------------
//                val contains = viewModel.user.value?.friends?.contains(user.username)
        var contains by remember {
            mutableStateOf(viewModel.user.value?.friends?.contains(itemUser.username))
        }
        if (itemUser.username != viewModel.user.value?.username) {
            if (contains == true ||_isUserFriend.value == true) {
                Log.d("HERE", "HERE")
                Button(onClick = {
                    viewModel.removeFromFriends(itemUser.username)
//                            vm.isUserFriend.value = false
                    contains = false
                    isRequestSent.value = false
                }) {
                    Row {
                        Text("Friend")
                        Icon(Icons.Filled.Done, contentDescription = "Is friend")
                    }
                }
            }
            else if (isRequestSent.value /*|| aI.valueisU.value == true*/) {
                Button(onClick = {
                    viewModel.removeFromFriends(itemUser.username)
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
                    viewModel.sendFriendRequest(itemUser.username)
                    isRequestSent.value = true
                }) {
                    Icon(
                        painterResource(R.drawable.friend_add),
                        contentDescription = "Send friend request")
                }
            }
        }
        else
        {
            Text("YOU")
        }
    }
}