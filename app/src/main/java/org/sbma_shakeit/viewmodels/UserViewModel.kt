package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    val allUsers = mutableStateListOf<User>()
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

        }
    }

    fun sendFriendRequest(receiver: String) {
        user.value?.let {
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

    fun addToFriends(username: String) {
        val currentFriends = user.value?.friends
        val updatedFriends = currentFriends?.plus(listOf(username))
        if (updatedFriends != null) {
            updateFriends(updatedFriends)
        }
    }

    fun removeFromFriends(username: String) {
        val currentFriends = user.value?.friends
        val updatedFriends = currentFriends?.minus(listOf(username).toSet())
        if (updatedFriends != null) {
            updateFriends(updatedFriends)
        }
    }

    private fun updateFriends(updatedFriends: List<String>) {
        db.collection(Collections.USERS)
            .document(userPath)
            .update(UserKeys.FRIENDS, updatedFriends)
            .addOnSuccessListener {
                viewModelScope.launch {
                    user.value = userProvider.getCurrentUser()
                    getUsers()
                }
            }
    }

    fun isUserFriend(username: String): Boolean =
        user.value?.friends?.any {
            it == username
        } ?: false

    private fun getUsers() {
        db.collection(Collections.USERS)
            .get()
            .addOnSuccessListener { result ->
                allUsers.clear()
                for (user in result) {
                    val userToAdd = user.toObject(User::class.java)
                    allUsers.add(userToAdd)
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