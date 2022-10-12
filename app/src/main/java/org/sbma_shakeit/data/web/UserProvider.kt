package org.sbma_shakeit.data.web

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
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
    suspend fun getAllUsers(): List<User> {
        val def = CompletableDeferred<List<User>>()
        userCollection
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<User>()
                Log.d("SIZE IS", result.size().toString())
                for (user in result) {
                    val userToAdd = user.toObject(User::class.java)
                    list.add(userToAdd)
                }
                def.complete(list)
            }
        return def.await()
    }

    /**
     * Get user object by its username
     * */
    suspend fun getUserByUsername(username: String): User {
        val def = CompletableDeferred<User>()
        userCollection
            .whereEqualTo(UserKeys.USERNAME, username)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    val res = it.documents.first()
//                    userPath = res.id
                    val userToAdd = res.toObject(User::class.java)
                    if (userToAdd != null) def.complete(userToAdd)
                }
            }
        return def.await()
    }

    /**
     * Get signed in user by users email
     * */
    suspend fun getCurrentUser(): User {
        val def = CompletableDeferred<User>()
        userCollection
            .whereEqualTo(UserKeys.EMAIL, usersEmail)
            .get()
            .addOnSuccessListener { result ->
                if (result.documents.isNotEmpty()) {
                    val res = result.documents.first()
                    val userToAdd = res.toObject(User::class.java)
                    if (userToAdd != null) def.complete(userToAdd)
                }
            }
        return def.await()
    }

    /**
     * Get users firestore id
     * */
    suspend fun getUserPath(username: String): String {
        val def = CompletableDeferred<String>()
        userCollection
            .whereEqualTo(UserKeys.USERNAME, username)
            .get().addOnSuccessListener {
                if (it.documents.first() != null) def.complete(it.documents.first().id)
            }
        return def.await()
    }
}