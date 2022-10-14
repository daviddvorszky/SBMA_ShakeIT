package org.sbma_shakeit.data.web

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB

class ShakeProvider {
    private val db = Firebase.firestore
    private val shakeCollection = db.collection(FirestoreCollections.SHAKES)

    fun saveShake(shake: Shake, database: ShakeItDB) {
        shakeCollection.add(shake)
            .addOnSuccessListener { ref ->
                shake.id = ref.id
                GlobalScope.launch(Dispatchers.IO) {
                    database.shakeDao().insert(shake)
                }
            }
    }

    suspend fun getShakesOfUser(username: String): List<Shake> {
        val def = CompletableDeferred<List<Shake>>()
        shakeCollection.whereEqualTo("parent", username)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Shake>()
                Log.d("ShakeProvider", "Size: ${result.size()}")
                for (shake in result) {
                    val shakeToAdd = shake.toObject(Shake::class.java)
                    list.add(shakeToAdd)
                }
                def.complete(list)
            }
        return def.await()
    }

    suspend fun getShakeById(id: String): Shake? {
        try {
            if (id == "") return null
            val result = shakeCollection.document(id).get().await()
            return result.toObject(Shake::class.java)
        } catch (e: Exception) {
            throw e
        }
    }


    fun getShakesFromLocal(database: ShakeItDB): LiveData<List<Shake>> {
        return database.shakeDao().getAll()
    }
}