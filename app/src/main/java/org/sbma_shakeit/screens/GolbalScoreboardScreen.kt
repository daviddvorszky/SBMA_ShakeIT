package org.sbma_shakeit.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sbma_shakeit.components.TabsView
import org.sbma_shakeit.components.UserList
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun GlobalScoreboardScreen(
    navController: NavController,
    vm: UserViewModel = UserViewModel(LocalContext.current.applicationContext as Application)
) {
    val allUsers = vm.getAll().observeAsState(listOf())
    val friendList = remember { mutableStateOf(vm.friends) }
    val list = listOf("Global", "Friends")
    val shakeTypes = listOf("Long", "Violent", "Quick")
    val selectedIndex = remember { mutableStateOf(0) }
    val selectedShakeIndex = remember { mutableStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(5.dp)) {
        TabsView(selectedIndex = selectedIndex, headers = list)
        Text("Type")
        TabsView(selectedIndex = selectedShakeIndex, headers = shakeTypes)
        Spacer(Modifier.height(5.dp))
        if (selectedIndex.value == 0)
            UserList(viewModel = vm, allUsers)
        else
            UserList(viewModel = vm, friendList)
    }
}