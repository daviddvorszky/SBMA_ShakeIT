package org.sbma_shakeit.viewmodels.users

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.web.FriendRequest
import org.sbma_shakeit.data.web.FriendsProvider
import org.sbma_shakeit.data.web.UserProvider

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val roomDb = ShakeItDB.get(application)
    private val friendsProvider = FriendsProvider()
    private val userProvider = UserProvider()
    private val currentUser = MutableLiveData<User?>(null)

    private val _friendRequests = mutableStateListOf<FriendRequest>()
    var friendRequests: List<FriendRequest> = _friendRequests

    private val _friends = mutableStateListOf<User>()
    val friends: List<User> = _friends

    init {
        viewModelScope.launch {
            currentUser.value = userProvider.getCurrentUser()
            if (currentUser.value != null) {
                _friendRequests.addAll(friendsProvider.getFriendRequests(currentUser.value!!.username))
            }
            _friends.addAll(friendsProvider.getFriends())
        }
        viewModelScope.launch(Dispatchers.IO) {
            updateUserList()
        }
    }

    fun getCurrentUser(): LiveData<User> {
        val userEmail = Firebase.auth.currentUser?.email
        return roomDb.userDao().getUserByEmail(userEmail ?: "")
    }

    /**
     * Update local user list by inserting user list from firestore to room
     * */
    private suspend fun updateUserList() {
        val users = userProvider.getAllUsers()
        val oldCount = roomDb.userDao().getUserCount()
        // Delete all if users have been removed from firestore
        if (users.size < oldCount) roomDb.userDao().deleteAll()
        roomDb.userDao().insertAll(users)
    }

    fun removeUser(userName: String? = currentUser.value?.username) {
        userName ?: return
        viewModelScope.launch {
            userProvider.removeUser(userName)
        }
    }

    fun sendFriendRequest(receiver: String) {
        currentUser.value?.let {
            if (it.friends.friends.contains(receiver)) return
            friendsProvider.sendFriendRequest(receiver, it.username)
        }
    }

    fun removeFriendRequest(receiver: String, sender: String) {
        viewModelScope.launch {
            friendsProvider.removeFriendRequest(receiver, sender)
            // Update UI
            _friendRequests.remove(FriendRequest(receiver, sender))
        }
    }

    fun acceptFriendRequest(username: String, friend: String) {
        viewModelScope.launch {
            friendsProvider.acceptFriendRequest(username, friend)
            // Update UI
            _friendRequests.remove(FriendRequest(receiver = username, sender = friend))
            val friendObj = userProvider.getUserByUsername(friend)
            _friends.add(friendObj)
        }
    }

    fun removeFromFriends(friendsName: String) {
        val cUser = currentUser.value ?: return
        // Remove friend from users friend list
        val currentFriends = cUser.friends.friends
        val updatedFriends = currentFriends.minus(friendsName)
        // Remove friend request if that existed
        removeFriendRequest(friendsName, cUser.username)
        viewModelScope.launch {
            val friend = userProvider.getUserByUsername(friendsName)
            // Remove user from friends friend list
            var friendsFriendList = friend.friends.friends
            friendsFriendList = friendsFriendList.minus(cUser.username)
            // Update both users friend lists in firestore
            friendsProvider.updateFriends(cUser.username, updatedFriends)
            friendsProvider.updateFriends(friendsName, friendsFriendList)
            _friends.removeIf { it.username == friendsName }
            delay(4000)
            currentUser.value = userProvider.getCurrentUser()
            updateUserList()
        }
    }
}