package org.sbma_shakeit.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.data.web.UserProvider

class ScoreboardViewModel: ViewModel() {
    private val sp = ShakeProvider()
    private val up = UserProvider()

    private val _longShakesAll = mutableStateListOf<User>()
    val longShakesAll: List<User> = _longShakesAll

    private val _violentShakesAll = mutableStateListOf<User>()
    val violentShakesAll: List<User> = _violentShakesAll

    private val _quickShakesAll = mutableStateListOf<User>()
    val quickShakesAll: List<User> = _quickShakesAll

    private val _longShakesFriends = mutableStateListOf<User>()
    val longShakesFriends: List<User> = _longShakesFriends

    private val _violentShakesFriends = mutableStateListOf<User>()
    val violentShakesFriends: List<User> = _violentShakesFriends

    private val _quickShakesFriends = mutableStateListOf<User>()
    val quickShakesFriends: List<User> = _quickShakesFriends

    init {
        viewModelScope.launch {
            // TODO: get values for every list
        }
    }
    // TODO: Order all users list by shake records
    private fun getGlobalLists(list: List<User>) {
//        _longShakesAll.addAll()
//        _violentShakesAll.addAll()
//        _quickShakesAll.addAll()
    }
    // TODO: Order friend list by shake records
    private fun getFriendsLists(list: List<User>) {
//        _longShakesFriends.addAll()
//        _violentShakesFriends.addAll()
//        _quickShakesFriends.addAll()
    }
}