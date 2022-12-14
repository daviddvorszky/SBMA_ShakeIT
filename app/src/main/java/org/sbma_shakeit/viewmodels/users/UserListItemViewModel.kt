package org.sbma_shakeit.viewmodels.users

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.web.FriendRequest
import org.sbma_shakeit.data.web.FriendsProvider
import org.sbma_shakeit.data.web.UserProvider

/**
 * ViewModel for user item in user list
 * */
class UserListItemViewModel(itemUser: String) : ViewModel() {

    val isCurrentUser = mutableStateOf(false)
    val isUserFriend = mutableStateOf(false)
    val isSentFriendReq = mutableStateOf(false)

    val shake = mutableStateOf<Shake?>(null)

    private val userProvider = UserProvider()
    private val friendsProvider = FriendsProvider()
    private var friendRequests = listOf<FriendRequest>()

    init {
        viewModelScope.launch {
            val currentUser = userProvider.getCurrentUser()
            isCurrentUser.value = isCurrentUser(currentUser, itemUser)
            friendRequests = friendsProvider.getFriendRequests(itemUser)
            isSentFriendReq.value = isRequestedForFriend(currentUser.username, itemUser)
            isUserFriend.value = isUserFriend(currentUser, itemUser)
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