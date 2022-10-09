package org.sbma_shakeit.data.web

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
     * Add all users from firestore to the given list
     * */
    fun getAllUsers(listToAdd: MutableList<User>) {
        userCollection
            .get()
            .addOnSuccessListener { result ->
                listToAdd.clear()
                for (user in result) {
                    val userToAdd = user.toObject(User::class.java)
                    listToAdd.add(userToAdd)
                }
            }
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