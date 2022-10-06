package org.sbma_shakeit.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendsProvider : UserProvider() {
    private val db = Firebase.firestore
    private val userCollection = db.collection(Collections.USERS)

    fun getFriendRequests(username: String, friendRequests: MutableList<FriendRequest>) {
        db.collection(Collections.FRIEND_REQUEST)
            .whereEqualTo("receiver", username)
            .get()
            .addOnSuccessListener { result ->
                for (request in result) {
                    Log.d("gg", "got them")
                    val req = request.toObject(FriendRequest::class.java)
                    friendRequests.add(req)
                }
            }
    }

    fun sendFriendRequest(receiver: String, sender: String) {
        val request = object {
            val receiver = receiver
            val sender = sender
        }
        db.collection(Collections.FRIEND_REQUEST)
            .add(request)
            .addOnSuccessListener {

            }
    }

    fun removeFriendRequest(receiver: String, sender: String) {
        db.collection(Collections.FRIEND_REQUEST)
            .whereEqualTo(FriendRequestKeys.RECEIVER, receiver)
            .whereEqualTo(FriendRequestKeys.SENDER, sender)
            .get()
            .addOnSuccessListener {
                Log.d("FRIEND REQS", "FOUND ${it.documents.size}")
                for (req in it.documents) {
                    db.collection(Collections.FRIEND_REQUEST)
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
        val oldFriends = currentUser.friends
        val newFriends = oldFriends.plus(friend)
        val userPath = getUserPath(user)
        userCollection
            .document(userPath)
            .update(UserKeys.FRIENDS, newFriends)
    }
}