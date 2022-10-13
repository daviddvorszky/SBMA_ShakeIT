package org.sbma_shakeit.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.data.web.ShakeProvider
import org.sbma_shakeit.data.web.UserProvider

class HistoryViewModel: ViewModel() {
    private val sp = ShakeProvider()
    private val up = UserProvider()

    private val _allShakes = mutableStateListOf<Shake>()
    val allShakes: List<Shake> = _allShakes

    private val _longShakes = mutableStateListOf<Shake>()
    val longShakes: List<Shake> = _longShakes

    private val _violentShakes = mutableStateListOf<Shake>()
    val violentShakes: List<Shake> = _violentShakes

    private val _quickShakes = mutableStateListOf<Shake>()
    val quickShakes: List<Shake> = _quickShakes

    init {
        viewModelScope.launch {
            val cUser = up.getCurrentUser()
            val shakes = sp.getShakesOfUser(cUser.username)
            _allShakes.addAll(shakes)
            _longShakes.addAll(shakes.filter { it.type == Shake.TYPE_LONG })
            _violentShakes.addAll(shakes.filter { it.type == Shake.TYPE_VIOLENT })
            _quickShakes.addAll(shakes.filter { it.type == Shake.TYPE_QUICK })
        }
    }
}