package org.sbma_shakeit.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred

open class UserProvider {
    private val db = Firebase.firestore
    private val usersEmail = Firebase.auth.currentUser?.email

    suspend fun getUserByUsername(username: String): User {
        val def = CompletableDeferred<User>()
        db.collection(Collections.USERS)
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

    suspend fun getCurrentUser(): User {
        val def = CompletableDeferred<User>()
        db.collection(Collections.USERS)
            .whereEqualTo(UserKeys.EMAIL, usersEmail)
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

    suspend fun getUserPath(username: String): String {
        val def = CompletableDeferred<String>()
        db.collection(Collections.USERS)
            .whereEqualTo(UserKeys.USERNAME, username)
            .get().addOnSuccessListener {
                if (it.documents.first() != null) def.complete(it.documents.first().id)
            }
        return def.await()
    }
}