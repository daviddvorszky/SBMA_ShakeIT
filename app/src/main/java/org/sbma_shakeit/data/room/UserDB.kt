package org.sbma_shakeit.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(User::class)], version = 1)
@TypeConverters(TypeConverter::class)
abstract class UserDB: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var sInstance: UserDB? = null
        @Synchronized
        fun get(context:Context) : UserDB {
            if (sInstance == null) {
                sInstance =
                    Room.databaseBuilder(context.applicationContext,
                    UserDB::class.java, "users.db").build()
            }
            return sInstance!!
        }
    }
}