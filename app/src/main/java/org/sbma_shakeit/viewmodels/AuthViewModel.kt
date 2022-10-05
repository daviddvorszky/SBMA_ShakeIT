package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.Collections
import org.sbma_shakeit.data.User
import org.sbma_shakeit.data.UserKeys

class AuthViewModel : ViewModel() {
    val auth = MutableLiveData<FirebaseAuth>()
    private val db = Firebase.firestore
    private val userCollection = db.collection(Collections.USERS)
    private var usernames =
        MutableLiveData<MutableList<String>>(mutableListOf())

    init {
        viewModelScope.launch {
            getUserNames()
        }
    }

    fun createUser(user: User) {
        userCollection
            .add(user)
            .addOnSuccessListener {
                Log.d("CREATE USER", "SUCCESS")
            }
            .addOnFailureListener {
                Log.d("CREATE USER", "FAILED", it)
            }
    }

    fun isUsernameTaken(username: String): Boolean {
        return usernames.value?.any {
            it == username
        } ?: false
    }

    private fun getUserNames() {
        userCollection
            .get()
            .addOnSuccessListener { result ->
                for (user in result) {
                    val username = user.data[UserKeys.USERNAME]
                    usernames.value?.add(username.toString())
                }
            }
    }
}