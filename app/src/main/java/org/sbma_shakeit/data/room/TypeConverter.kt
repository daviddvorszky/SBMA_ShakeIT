package org.sbma_shakeit.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson

class TypeConverter {
    @TypeConverter
    fun fromFriendsToJSON(friends: Friends): String {
        return Gson().toJson(friends)
    }
    @TypeConverter
    fun fromJSONtoFriends(json: String): Friends {
        return Gson().fromJson(json, Friends::class.java)
    }
}