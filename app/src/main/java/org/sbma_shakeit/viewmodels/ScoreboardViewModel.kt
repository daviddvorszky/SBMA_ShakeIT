package org.sbma_shakeit.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.room.UserWithShakes
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.data.web.UserProvider

data class UserWithShake(
    val user: User,
    val long: Shake?,
    val violent: Shake?,
    val quick: Shake?
)

class ScoreboardViewModel: ViewModel() {
    private val sp = ShakeProvider()
    private val up = UserProvider()

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
            val cUser = up.getCurrentUser()
            val allUsers = up.getAllUsers()
            val usersWithShakes = mutableListOf<UserWithShake>()
            for (user in allUsers) {
                val long = sp.getShakeById(user.longShake ?: "")
                val quick = sp.getShakeById(user.quickShake ?: "")
                val violent = sp.getShakeById(user.violentShake ?: "")
                val u = UserWithShake(user, long, quick, violent)
                usersWithShakes.add(u)
            }
            getGlobalLists(usersWithShakes)

            val friendsWithShakes = mutableListOf<UserWithShake>()
            for (userWS in usersWithShakes) {
                if (cUser.friends.friends.contains(userWS.user.username)) {
                    friendsWithShakes.add(userWS)
                }
            }
            getFriendsLists(friendsWithShakes)
        }
    }
    // TODO: Order all users list by shake records
    private fun getGlobalLists(list: List<UserWithShake>) {
        _longShakesAll.addAll(list.sortedBy { it.long?.duration }.reversed())
        _violentShakesAll.addAll(list.sortedBy { it.violent?.score }.reversed())
        _quickShakesAll.addAll(list.sortedBy { it.quick?.score }.reversed())
    }
    // TODO: Order friend list by shake records
    private fun getFriendsLists(list: List<UserWithShake>) {
        _longShakesFriends.addAll(list.sortedBy { it.long?.duration }.reversed())
        _violentShakesFriends.addAll(list.sortedBy { it.violent?.score }.reversed())
        _quickShakesFriends.addAll(list.sortedBy { it.quick?.score }.reversed())
    }
}