package org.sbma_shakeit.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.sbma_shakeit.data.room.Shake

class SingleShakeViewModel:ViewModel() {

    var shake:Shake by mutableStateOf(Shake())
    var image: Bitmap? = null

    fun setNewShake(newShake: Shake){
        shake = newShake
        downloadImage()
    }

    private fun downloadImage(){
        val storageRef = Firebase.storage.reference
        val imgname = shake.imagePath.ifEmpty { "no_image.png" }
        Log.d("pengb_download", "$imgname, $${shake.imagePath}")
        Log.d("pengb_download", shake.toString())
        val fileRef = storageRef.child(imgname)
        val ONE_MEGABYTE = 1024 * 1024
        fileRef.getBytes(ONE_MEGABYTE.toLong()).addOnSuccessListener { bytearray ->
            image = byteArrayToBitmap(bytearray)
        }
    }

    private fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}