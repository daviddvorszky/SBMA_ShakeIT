package org.sbma_shakeit.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.firestore.Exclude

@Entity
data class Shake(
    @PrimaryKey @Exclude var id: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "score") val score: Float,
    @ColumnInfo(name = "duration") val duration: Long,
    @ColumnInfo(name = "parent_username") val parent: String,
    @ColumnInfo(name = "image_path") val imagePath: String?,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "latitude") val latitude: Float,
){
    companion object{
        const val TYPE_LONG = 0
        const val TYPE_QUICK = 1
        const val TYPE_VIOLENT = 2
    }
    override fun toString(): String {
        return "Shake $id, type: $type, score: $score, duration: $duration, parent: $parent, image path: $imagePath, location: [$latitude, $longitude]"
    }
}

@Dao
interface ShakeDao{
    @Query("SELECT * FROM shake")
    fun getAll(): LiveData<List<Shake>>

    @Query("SELECT * FROM shake WHERE id = :id LIMIT 1")
    fun getById(id: Int): LiveData<Shake>

    @Insert
    fun insert(shake: Shake)

    @Delete
    fun delete(shake: Shake)
}