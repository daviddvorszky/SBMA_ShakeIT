package org.sbma_shakeit.viewmodels

import android.app.Application
import android.util.Log
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
import org.sbma_shakeit.data.FriendRequest
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.room.UserDB
import org.sbma_shakeit.data.web.FriendsProvider
import org.sbma_shakeit.data.web.UserProvider

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val roomDb = UserDB.get(application)

    val user = MutableLiveData<User?>(null)

    private val _allUsers = mutableStateListOf<User>()
    val allUsers: List<User> = _allUsers

    private val _friendRequests = mutableStateListOf<FriendRequest>()
    val friendRequests: List<FriendRequest> = _friendRequests

    private val friendsProvider = FriendsProvider()
    private val userProvider = UserProvider()

    private val _friends = mutableStateListOf<User>()
    val friends: List<User> = _friends

    init {
        Log.d("USER VM", "INIT")
        viewModelScope.launch {
            userProvider.getAllUsers(_allUsers)
            user.value = userProvider.getCurrentUser()
            friendsProvider.getFriendRequests(user.value!!.username, _friendRequests)
            friendsProvider.getFriends(_friends)
            delay(5000)
        }
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            updateUserList()
            delay(1000)
            Log.d("getAll", getAll().value?.size.toString())
        }
    }

    //---------------------------------------------------------------------------
    fun getAll(): LiveData<List<User>> =
        roomDb.userDao().getAll()

    fun getCurrentUser(): LiveData<User> {
        val userEmail = Firebase.auth.currentUser?.email
        return roomDb.userDao().getUserByEmail(userEmail ?: "")
    }

    private suspend fun updateUserList() {
        roomDb.userDao().insertAll(allUsers)
    }

//---------------------------------------------------------------------------


//    fun orderUsersByLong() {
//        val orderedList = allUsers.sortedBy {
//            it.longShake.time
//        }.reversed()
//        _allUsers.clear()
//        _allUsers.addAll(orderedList)
//    }
//    fun orderUsersByViolent() {
//        val orderedList = allUsers.sortedBy {
//            it.violentShake.score
//        }.reversed()
//        _allUsers.clear()
//        _allUsers.addAll(orderedList)
//    }
//    fun orderUsersByQuick() {
//        val orderedList = allUsers.sortedBy {
//            it.quickShake.score
//        }.reversed()
//        _allUsers.clear()
//        _allUsers.addAll(orderedList)
//    }

    fun sendFriendRequest(receiver: String) {
        user.value?.let {
            if (it.friends.friends.contains(receiver)) return
            friendsProvider.sendFriendRequest(receiver, it.username)
        }
    }

    fun removeFriendRequest(receiver: String, sender: String) {
        friendsProvider.removeFriendRequest(receiver, sender)
    }

    fun acceptFriendRequest(username: String, friend: String) {
        _friendRequests.clear()
        viewModelScope.launch {
            friendsProvider.acceptFriendRequest(username, friend)
            delay(1000)
            friendsProvider.getFriendRequests(user.value!!.username, _friendRequests)
        }
    }

    fun removeFromFriends(friendsName: String) {
        val currentUser = user.value ?: return
        // Remove friend from users friend list
        val currentFriends = currentUser.friends.friends
        val updatedFriends = currentFriends.minus(friendsName)
        // Remove friend request if that existed
        removeFriendRequest(friendsName, currentUser.username)
        viewModelScope.launch {
            val friend = userProvider.getUserByUsername(friendsName)
            // Remove user from friends friend list
            var friendsFriendList = friend.friends.friends
            friendsFriendList = friendsFriendList.minus(currentUser.username)
            // Update both users friend lists in firestore
            friendsProvider.updateFriends(currentUser.username, updatedFriends)
            friendsProvider.updateFriends(friendsName, friendsFriendList)
            delay(4000)
            user.value = userProvider.getCurrentUser()
            userProvider.getAllUsers(_allUsers)
        }

    }
}