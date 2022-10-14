package org.sbma_shakeit.data.web

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.ShakeItDB
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class ShakeProvider {
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val shakeCollection = db.collection(FirestoreCollections.SHAKES)
    private val storageRef = storage.reference

    suspend fun saveShake(shake: Shake, database: ShakeItDB, image: File?){
        if(image != null){
            val imageRef = storageRef.child(image.name)
            val file = Uri.fromFile(image)
            val uploadTask = imageRef.putFile(file)
            val urlTask = uploadTask.continueWithTask { task ->
                if(!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    val downloadUri = task.result
                    Log.d("pengb", downloadUri?.path.toString())
                    shake.imagePath = downloadUri?.path.toString()?:""
                }else{
                    Log.w("Shake Image Upload", "Something went wrong... (ShakeProvider.kt)")
                }
            }

            urlTask.await()
        }

        shakeCollection.add(shake)
            .addOnSuccessListener { ref ->
                shake.id = ref.id
                GlobalScope.launch(Dispatchers.IO) {
                    database.shakeDao().insert(shake)
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

    suspend fun getShakeById(id: String): Shake? =
        try {
            val result = shakeCollection.document(id).get().await()
            result.toObject(Shake::class.java)
        } catch (e: Exception) {
            throw e
        }

    fun getShakesFromLocal(database: ShakeItDB): LiveData<List<Shake>> {
        return database.shakeDao().getAll()
    }
}