package org.sbma_shakeit.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

data class Friends(
    val friends: List<String> = listOf()
)

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    var email: String = "",
    var friends: Friends = Friends(),
    var quickShake: Int? = null,
    var violentShake: Int? = null,
    var longShake: Int? = null,
)

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserByUsername(username: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userEntityEntities: List<User>)

    @Query("DELETE FROM user")
    fun deleteAll()
}