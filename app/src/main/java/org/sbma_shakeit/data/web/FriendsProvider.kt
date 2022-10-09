package org.sbma_shakeit.data.web

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.sbma_shakeit.data.*
import org.sbma_shakeit.data.room.Friends
import org.sbma_shakeit.data.room.User

/**
 Provides and handles friends related data from firestore
 */
class FriendsProvider : UserProvider() {

    private val db = Firebase.firestore
    private val userCollection = db.collection(FirestoreCollections.USERS)
    private val friendRequestCollection = db.collection(FirestoreCollections.FRIEND_REQUESTS)

    /**
     * Add all users friends to the given list
     * */
    suspend fun getFriends(listToAdd: MutableList<User>) {
        val currentUser = getCurrentUser()
        for (friend in currentUser.friends.friends) {
            val userToAdd = getUserByUsername(friend)
            listToAdd.add(userToAdd)
        }
    }
    suspend fun updateFriends(user: String, updatedFriends: List<String>) {
        Log.d("UPDATE FRIENDS", "CALLED")
        val userPath = getUserPath(user)
        userCollection
            .document(userPath)
            .update(UserKeys.FRIENDS, Friends(updatedFriends))
            .addOnSuccessListener {

            }
    }
    /**
     Get friend requests for given user
     */
    fun getFriendRequests(username: String, friendRequests: MutableList<FriendRequest>) {
        friendRequestCollection
            .whereEqualTo(FriendRequestKeys.RECEIVER, username)
            .get()
            .addOnSuccessListener { result ->
                for (request in result) {
                    val req = request.toObject(FriendRequest::class.java)
                    friendRequests.add(req)
                }
            }
    }
    /**
    Creates a friend request to firestore
     */
    fun sendFriendRequest(receiver: String, sender: String) {
        val request = object {
            val receiver = receiver
            val sender = sender
        }
        friendRequestCollection
            .add(request)
            .addOnSuccessListener {

            }
    }

    /**
     Removes friend request from firestore
     */
    fun removeFriendRequest(receiver: String, sender: String) {
        friendRequestCollection
            .whereEqualTo(FriendRequestKeys.RECEIVER, receiver)
            .whereEqualTo(FriendRequestKeys.SENDER, sender)
            .get()
            .addOnSuccessListener {
                Log.d("FRIEND REQS", "FOUND ${it.documents.size}")
                for (req in it.documents) {
                    db.collection(FirestoreCollections.FRIEND_REQUESTS)
                        .document(req.id)
                        .delete()
                }
            }
    }

    /**
    Adds both users to each others friend lists and removes the request
     */
    suspend fun acceptFriendRequest(username: String, friend: String) {
        addToFriends(username, friend)
        addToFriends(friend, username)
        removeFriendRequest(username, friend)
    }

    private suspend fun addToFriends(user: String, friend: String) {
        val currentUser = getUserByUsername(user)
        val oldFriends = currentUser.friends.friends
        val newFriends = oldFriends.plus(friend)
        val userPath = getUserPath(user)
        userCollection
            .document(userPath)
            .update(UserKeys.FRIENDS, Friends(newFriends))
    }
}