package org.sbma_shakeit.data.web

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB

class ShakeProvider {
    private val db = Firebase.firestore
    private val shakeCollection = db.collection(FirestoreCollections.SHAKES)

    fun saveShake(shake: Shake, database: ShakeItDB){
        shakeCollection.add(shake)
            .addOnSuccessListener { ref ->
                shake.id = ref.id
                GlobalScope.launch(Dispatchers.IO) {
                    database.shakeDao().insert(shake)
                }
            }
    }
}