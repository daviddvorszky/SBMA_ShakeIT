package org.sbma_shakeit.data

data class User(
    val username: String = "",
    val email: String = "",
    val friends: List<String> = listOf()
)

data class FriendRequest(
    val receiver: String = "",
    val sender: String = ""
)
