package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.FriendRequest
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.web.FriendsProvider
import org.sbma_shakeit.data.web.UserProvider

/**
 * ViewModel for user item in user list
 * */
class UserListItemViewModel(itemUser: String) : ViewModel() {

    val isCurrentUser = mutableStateOf(false)
    val isUserFriend = mutableStateOf(false)
    val isSentFriendReq = mutableStateOf(false)

    private val userProvider = UserProvider()
    private val friendsProvider = FriendsProvider()
    private val friendRequests = mutableStateListOf<FriendRequest>()

    init {
        viewModelScope.launch {
            val cUser = userProvider.getCurrentUser()
            isCurrentUser.value = isCurrentUser(cUser, itemUser)
            friendsProvider.getFriendRequests(itemUser, friendRequests)
            delay(1000)
            isSentFriendReq.value = isRequestedForFriend(cUser.username, itemUser)
            isUserFriend.value = isUserFriend(cUser, itemUser)
        }
    }

    private fun isCurrentUser(currentUser: User, itemUser: String): Boolean =
        currentUser.username == itemUser

    private fun isUserFriend(currentUser: User, itemUser: String): Boolean =
        currentUser.friends.friends.contains(itemUser)

    private fun isRequestedForFriend(user: String, friend: String): Boolean =
        friendRequests.any {
            it.sender == user && it.receiver == friend
        }

}