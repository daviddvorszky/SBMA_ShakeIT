package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.*

class UserViewModel : ViewModel() {
    private lateinit var userPath: String
    val user = MutableLiveData<User?>(null)

    private val _allUsers = mutableStateListOf<User>()
    val allUsers : List<User> = _allUsers

    private val userEmail = Firebase.auth.currentUser?.email
    private val db = Firebase.firestore

    private val _friendRequests = mutableStateListOf<FriendRequest>()
    val friendRequests: List<FriendRequest> = _friendRequests

    private val friendsProvider = FriendsProvider()
    private val userProvider = UserProvider()

    private val _friends = mutableStateListOf<User>()
    val friends: List<User> = _friends

    init {
        Log.d("USER VM", "INIT")
        viewModelScope.launch {
            getUsers()
            user.value = userProvider.getCurrentUser()
            friendsProvider.getFriendRequests(user.value!!.username, _friendRequests)
            getFriends()
            userPath = userProvider.getUserPath(user.value!!.username)

        }
    }

    fun orderUsersByLong() {
        val orderedList = allUsers.sortedBy {
            it.longShake.time
        }.reversed()
        _allUsers.clear()
        _allUsers.addAll(orderedList)
    }
    fun orderUsersByViolent() {
        val orderedList = allUsers.sortedBy {
            it.violentShake.score
        }.reversed()
        _allUsers.clear()
        _allUsers.addAll(orderedList)
    }
    fun orderUsersByQuick() {
        val orderedList = allUsers.sortedBy {
            it.quickShake.score
        }.reversed()
        _allUsers.clear()
        _allUsers.addAll(orderedList)
    }

    fun sendFriendRequest(receiver: String) {
        user.value?.let {
            if (it.friends.contains(receiver)) return
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

    fun removeFromFriends(friend: String) {
        val currentFriends = user.value?.friends
        val updatedFriends = currentFriends?.minus(listOf(friend).toSet())
        user.value?.let {
            removeFriendRequest(friend, it.username)
        if (!updatedFriends.isNullOrEmpty()) {
            viewModelScope.launch {
                val _friend = userProvider.getUserByUsername(friend)
                var friendsFriendList = _friend.friends
                friendsFriendList = friendsFriendList.minus(it.username)
                updateFriends(it.username,updatedFriends)
                updateFriends(friend, friendsFriendList)
            }
        }
        }
    }

    private suspend fun updateFriends(user2: String, updatedFriends: List<String>) {
        val userPath2 = userProvider.getUserPath(user2)
        db.collection(Collections.USERS)
            .document(userPath2)
            .update(UserKeys.FRIENDS, updatedFriends)
            .addOnSuccessListener {
                viewModelScope.launch {
                    user.value = userProvider.getCurrentUser()
                    getUsers()
                }
            }
    }

    fun isRequestedForFriend(user: String, friend: String): State<Boolean> =
        mutableStateOf(friendRequests.any() {
            it.sender == user && it.receiver == friend
        })


    fun isUserFriend(username: String): State<Boolean> =
        mutableStateOf(user.value?.friends?.any {
            it == username
        } ?: false)


    private fun getUsers() {
        Log.d("getUsers", "called")
        db.collection(Collections.USERS)
            .get()
            .addOnSuccessListener { result ->
                _allUsers.clear()
                for (user in result) {
                    val userToAdd = user.toObject(User::class.java)
                    _allUsers.add(userToAdd)
                }
            }
    }

    private suspend fun getFriends() {
        if (user.value?.friends.isNullOrEmpty()) return
        for (friend in user.value?.friends!!) {
            val userToAdd = userProvider.getUserByUsername(friend)
            _friends.add(userToAdd)
        }
    }
}