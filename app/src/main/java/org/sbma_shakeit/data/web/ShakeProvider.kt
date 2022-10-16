package org.sbma_shakeit.data.web

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import java.io.File

class ShakeProvider {
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val shakeCollection = db.collection(FirestoreCollections.SHAKES)
    private val storageRef = storage.reference
    private val userProvider = UserProvider()

    suspend fun saveShake(shake: Shake, database: ShakeItDB, image: File?){
        if(image != null){
                val imageRef = storageRef.child(image.name)
                val file = Uri.fromFile(image)
                val uploadTask = imageRef.putFile(file)
                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("pengb", downloadUri?.lastPathSegment.toString())
                        shake.imagePath = downloadUri?.lastPathSegment.toString()
                    } else {
                        Log.w("Shake Image Upload", "Something went wrong... (ShakeProvider.kt)")
                    }
                }

                urlTask.await()
            }

        val bestShake = getBestShakeOfUser(shake.parent, shake.type)

        val shakeUpload = runBlocking {
            shakeCollection.add(shake).continueWith{ ref ->
                shake.id = ref.result.id
                Log.d("pengb_sp", shake.id)
                    database.shakeDao().insert(shake)
                }
        }
        shakeUpload.await()

        Log.d("pengb_sp", shake.id)
        when(shake.type){
            Shake.TYPE_LONG -> {
                if(shake.duration > bestShake.duration)
                    userProvider.updateLongRecord(shake.id)
            }
            Shake.TYPE_QUICK -> {
                if(shake.score > bestShake.score)
                    userProvider.updateQuickRecord(shake.id)
            }
            Shake.TYPE_VIOLENT -> {
                if(shake.score > bestShake.score)
                    userProvider.updateViolentRecord(shake.id)
            }
        }
    }

    suspend fun getShakesOfUser(username: String): List<Shake>{
        val def = CompletableDeferred<List<Shake>>()
        shakeCollection.whereEqualTo("parent", username)
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Shake>()
                Log.d("ShakeProvider", "Size: ${result.size()}")
                for(shake in result){
                    val shakeToAdd = shake.toObject(Shake::class.java)
                    list.add(shakeToAdd)
                }
                def.complete(list)
            }
        return def.await()
    }

    private suspend fun getBestShakeOfUser(username: String, type: Int): Shake{
        val def = CompletableDeferred<Shake>()
        val scoreType = when(type){
            0 -> "duration"
            else -> "score"
        }
        shakeCollection
            .whereEqualTo("parent", username)
            .whereEqualTo("type", type)
            .orderBy(scoreType)
            .limitToLast(1)
            .get()
            .addOnSuccessListener { result ->
                val shake = result.first().toObject(Shake::class.java)
                def.complete(shake)
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
}