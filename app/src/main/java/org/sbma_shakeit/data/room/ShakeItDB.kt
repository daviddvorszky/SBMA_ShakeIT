package org.sbma_shakeit.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(User::class), (Shake::class)], version = 3)
@TypeConverters(TypeConverter::class)
abstract class ShakeItDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shakeDao(): ShakeDao

    companion object {
        private var sInstance: ShakeItDB? = null
        @Synchronized
        fun get(context:Context) : ShakeItDB {
            if (sInstance == null) {
                sInstance =
                    Room.databaseBuilder(context.applicationContext,
                    ShakeItDB::class.java, "users.db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return sInstance!!
        }
    }
}