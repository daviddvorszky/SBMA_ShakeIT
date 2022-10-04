package org.sbma_shakeit.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.sbma_shakeit.data.User

class AuthViewModel : ViewModel() {
    val auth = MutableLiveData<FirebaseAuth>()
    private val db = Firebase.firestore
    private var usernames =
        MutableLiveData<MutableList<String>>(mutableListOf())

    init {
        getUserNames()
    }

    fun createUser(user: User) {
        db.collection("users")
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
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (user in result) {
                    val username = user.data["username"]
                    usernames.value?.add(username.toString())
                }
            }
    }
}