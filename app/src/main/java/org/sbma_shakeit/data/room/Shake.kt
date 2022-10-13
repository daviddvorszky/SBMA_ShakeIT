package org.sbma_shakeit.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.firestore.Exclude

@Entity
data class Shake(
    @PrimaryKey @Exclude var id: String = "",
    @ColumnInfo(name = "type") var type: Int = -1,
    @ColumnInfo(name = "score") var score: Float = 0f,
    @ColumnInfo(name = "duration") var duration: Long = 0,
    @ColumnInfo(name = "parent_username") var parent: String = "",
    @ColumnInfo(name = "image_path") var imagePath: String = "",
    @ColumnInfo(name = "longitude") var longitude: Float = 0f,
    @ColumnInfo(name = "latitude") var latitude: Float = 0f,
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