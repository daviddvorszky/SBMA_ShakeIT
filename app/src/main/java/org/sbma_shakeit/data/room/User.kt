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
    var quickShake: String? = null,
    var violentShake: String? = null,
    var longShake: String? = null,
)

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserByUsername(username: String): LiveData<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): LiveData<User>

    @Query("SELECT COUNT(username) FROM user")
    suspend fun getUserCount() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM user")
    fun deleteAll()
}