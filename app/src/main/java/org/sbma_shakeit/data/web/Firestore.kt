package org.sbma_shakeit.data.web

data class FriendRequest(
    val receiver: String = "",
    val sender: String = ""
)

object UserKeys {
    const val USERNAME = "username"
    const val EMAIL = "email"
    const val FRIENDS = "friends"
}

object FriendRequestKeys {
    const val RECEIVER = "receiver"
    const val SENDER = "sender"
}

object FirestoreCollections {
    const val USERS = "users"
    const val FRIEND_REQUESTS = "friendRequest"
}