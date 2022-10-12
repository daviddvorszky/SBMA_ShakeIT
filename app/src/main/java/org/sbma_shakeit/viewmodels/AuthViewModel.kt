package org.sbma_shakeit.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.data.web.FirestoreCollections
import org.sbma_shakeit.data.web.UserKeys

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val auth = MutableLiveData<FirebaseAuth>()
    private val shakeItDB = ShakeItDB.get(application)
    private val userCollection =
        Firebase.firestore.collection(FirestoreCollections.USERS)
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
                viewModelScope.launch(Dispatchers.IO) {
                    shakeItDB.userDao().insertUser(user)
                }
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