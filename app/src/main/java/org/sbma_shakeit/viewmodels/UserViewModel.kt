package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.Collections
import org.sbma_shakeit.data.User
import org.sbma_shakeit.data.UserKeys

class UserViewModel: ViewModel() {
    val user = MutableLiveData<User?>(null)
    val allUsers = MutableLiveData<MutableList<User>>(mutableListOf())
    private val userEmail = Firebase.auth.currentUser?.email
    private val db = Firebase.firestore

    init {
        Log.d("USER VM", "INIT")
        viewModelScope.launch {
            getCurrentUser()
            getUsers()
        }
    }

    private fun getCurrentUser() {
        userEmail ?: return
        db.collection(Collections.USERS)
            .whereEqualTo(UserKeys.EMAIL, userEmail)
            .get()
            .addOnSuccessListener {
                val result = it.documents.first().data
                user.value = resultToUser(result as Map<String, Any>)
            }
    }
    private fun getUsers() {
        db.collection(Collections.USERS)
            .get()
            .addOnSuccessListener { result ->
                for (user in result) {
                    val userToAdd = resultToUser(user.data)
                    allUsers.value?.add(userToAdd)
                }
            }
    }
    private fun resultToUser(result: Map<String, Any>) : User =
        User(
            username = result[UserKeys.USERNAME] as String,
            email = result[UserKeys.EMAIL] as String
        )
}