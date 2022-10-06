package org.sbma_shakeit.data

import com.google.firebase.firestore.GeoPoint
import java.util.*

data class User(
    val username: String = "",
    val email: String = "",
    val friends: List<String> = listOf(),
    val quickShake: QuickShake = QuickShake(),
    val violentShake: ViolentShake = ViolentShake(),
    val longShake: LongShake = LongShake()
)
data class LongShake(
    val time: Long = 0,
    val created: Date = Date(),
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)
data class QuickShake(
    val score: Int = 0,
    val created: Date = Date(),
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)
data class ViolentShake(
    val score: Float = 0.0F,
    val created: Date = Date(),
    val location: GeoPoint = GeoPoint(0.0, 0.0)
)

data class FriendRequest(
    val receiver: String = "",
    val sender: String = ""
)
