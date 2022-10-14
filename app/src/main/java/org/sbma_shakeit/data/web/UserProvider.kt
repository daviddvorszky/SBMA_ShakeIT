package org.sbma_shakeit.data.web

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import org.sbma_shakeit.data.room.User

/**
 * Provides user related data from firestore
 * */
open class UserProvider {
    private val db = Firebase.firestore
    private val usersEmail = Firebase.auth.currentUser?.email
    private val userCollection = db.collection(FirestoreCollections.USERS)

    /**
     * Returns all users from firestore as a list
     * */
    suspend fun getAllUsers(): List<User> =
        try {
            val result = userCollection.get().await()
            result.toObjects(User::class.java)
        } catch (e: Exception) {
            Log.e("getAllUsers", e.toString())
            throw e
        }

    /**
     * Get user object by its username
     * */
    suspend fun getUserByUsername(username: String): User =
        try {
            val result = userCollection.whereEqualTo(UserKeys.USERNAME, username).get().await()
            result.first().toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("getUserByUsername", e.toString())
            throw e
        }

    /**
     * Get signed in user by users email
     * */
    suspend fun getCurrentUser(): User =
        try {
            val result =
                userCollection.whereEqualTo(UserKeys.EMAIL, usersEmail).get().await()
            result.first().toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("getCurrentUser", e.message ?: "error getting user")
            throw e
        }


    /**
     * Get users firestore id
     * */
    suspend fun getUserPath(username: String): String =
        try {
            val result =
                userCollection.whereEqualTo(UserKeys.USERNAME, username).get().await()
            result.first().id
        } catch (e: Exception) {
            throw e
        }

    /**
     * Removes user from firestore and authentication
     * */
    suspend fun removeUser(username: String) {
        val userPath = getUserPath(username)
        val userAuth = Firebase.auth.currentUser ?: return
        userCollection
            .document(userPath)
            .delete()
            .addOnSuccessListener {
                Log.d("removeUser", "removed $username")
            }
        userAuth
            .delete()
    }
    suspend fun updateLong(shakeId: String) {
        val cUser = getCurrentUser()
        val userPath = getUserPath(cUser.username)
        userCollection
            .document(userPath)
            .update(UserKeys.LONG_SHAKE, shakeId)
    }
    suspend fun updateViolent(shakeId: String) {
        val cUser = getCurrentUser()
        val userPath = getUserPath(cUser.username)
        userCollection
            .document(userPath)
            .update(UserKeys.VIOLENT_SHAKE, shakeId)
    }
    suspend fun updateQuick(shakeId: String) {
        val cUser = getCurrentUser()
        val userPath = getUserPath(cUser.username)
        userCollection
            .document(userPath)
            .update(UserKeys.QUICK_SHAKE, shakeId)
    }
}