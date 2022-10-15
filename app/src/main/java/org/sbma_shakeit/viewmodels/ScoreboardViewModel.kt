package org.sbma_shakeit.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.data.web.UserProvider

data class UserWithShake(
    val user: User,
    val long: Shake?,
    val violent: Shake?,
    val quick: Shake?
)

class ScoreboardViewModel: ViewModel() {
    private val shakeProvider = ShakeProvider()
    private val userProvider = UserProvider()

    private val _longShakesAll = mutableStateListOf<UserWithShake>()
    val longShakesAll: List<UserWithShake> = _longShakesAll

    private val _violentShakesAll = mutableStateListOf<UserWithShake>()
    val violentShakesAll: List<UserWithShake> = _violentShakesAll

    private val _quickShakesAll = mutableStateListOf<UserWithShake>()
    val quickShakesAll: List<UserWithShake> = _quickShakesAll

    private val _longShakesFriends = mutableStateListOf<UserWithShake>()
    val longShakesFriends: List<UserWithShake> = _longShakesFriends

    private val _violentShakesFriends = mutableStateListOf<UserWithShake>()
    val violentShakesFriends: List<UserWithShake> = _violentShakesFriends

    private val _quickShakesFriends = mutableStateListOf<UserWithShake>()
    val quickShakesFriends: List<UserWithShake> = _quickShakesFriends

    init {
        viewModelScope.launch {
            val usersWithShakes = getUsersWithShakes()
            addGlobalLists(usersWithShakes)

            val friendsWithShakes = getFriendsWithShakes(usersWithShakes)
            addFriendsLists(friendsWithShakes)
        }
    }
    private suspend fun getUsersWithShakes(): List<UserWithShake> {
        val allUsers = userProvider.getAllUsers()
        val usersWithShakes = mutableListOf<UserWithShake>()
        for (user in allUsers) {
            val long = shakeProvider.getShakeById(user.longShake ?: "")
            val quick = shakeProvider.getShakeById(user.quickShake ?: "")
            val violent = shakeProvider.getShakeById(user.violentShake ?: "")
            val userWithShakes = UserWithShake(user, long, quick, violent)
            usersWithShakes.add(userWithShakes)
        }
        return usersWithShakes
    }

    private suspend fun getFriendsWithShakes(usersWithShakes: List<UserWithShake>):
            List<UserWithShake> {
        val cUser = userProvider.getCurrentUser()
        val friendsWithShakes = mutableListOf<UserWithShake>()
        for (userWithShake in usersWithShakes) {
            if (cUser.friends.friends.contains(userWithShake.user.username)) {
                friendsWithShakes.add(userWithShake)
            }
        }
        return friendsWithShakes
    }

    private fun addGlobalLists(list: List<UserWithShake>) {
        _longShakesAll.addAll(list.sortedBy { it.long?.duration }.reversed())
        _violentShakesAll.addAll(list.sortedBy { it.violent?.score }.reversed())
        _quickShakesAll.addAll(list.sortedBy { it.quick?.score }.reversed())
    }

    private fun addFriendsLists(list: List<UserWithShake>) {
        _longShakesFriends.addAll(list.sortedBy { it.long?.duration }.reversed())
        _violentShakesFriends.addAll(list.sortedBy { it.violent?.score }.reversed())
        _quickShakesFriends.addAll(list.sortedBy { it.quick?.score }.reversed())
    }
}