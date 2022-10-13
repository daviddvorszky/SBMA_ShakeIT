package org.sbma_shakeit.data.web

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
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

    /**
     * Updates users friend list in firestore
     * */
    suspend fun updateFriends(user: String, updatedFriends: List<String>) {
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
    suspend fun getFriendRequests(friend: String): List<FriendRequest> =
        try {
            val result = friendRequestCollection
                .whereEqualTo(FriendRequestKeys.RECEIVER, friend).get().await()
            result.toObjects(FriendRequest::class.java)
        } catch (e: Exception) {
            throw e
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