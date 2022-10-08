package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.FriendRequest
import org.sbma_shakeit.data.FriendsProvider

class UserListItemViewModel(currentUser: String, itemUser: String) : ViewModel() {

    val isUserFriend = MutableLiveData(false)

    val isSentFriendReq = mutableStateOf(false)

    private val friendRequests = mutableStateListOf<FriendRequest>()
    private val friendsProvider = FriendsProvider()

    init {
        Log.d("userListVm", "INIT")
        viewModelScope.launch {
            friendsProvider.getFriendRequests(itemUser, friendRequests)
            delay(1000)
            isSentFriendReq.value = isRequestedForFriend(currentUser, itemUser)
        }
    }
    private fun isRequestedForFriend(user: String, friend: String): Boolean {
        Log.d("FRR", friendRequests.size.toString())
       return friendRequests.any() {
            it.sender == user && it.receiver == friend
        }
    }
  }