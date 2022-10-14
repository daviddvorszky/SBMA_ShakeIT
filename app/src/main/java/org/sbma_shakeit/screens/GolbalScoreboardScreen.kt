package org.sbma_shakeit.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sbma_shakeit.components.TabsView
import org.sbma_shakeit.components.UserList
import org.sbma_shakeit.viewmodels.ScoreboardViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun GlobalScoreboardScreen(
    navController: NavController,
    vm: UserViewModel = UserViewModel(LocalContext.current.applicationContext as Application)
) {
    val vm2 = ScoreboardViewModel()
    val allUsers = vm.getAll().observeAsState(listOf())
    val friendList = remember { mutableStateOf(vm.friends) }
    // Tabs
    val groupHeaders = listOf("Global", "Friends")
    val shakeTypes = listOf("Long", "Violent", "Quick")
    val selectedIndexGroup = remember { mutableStateOf(0) }
    val selectedShakeIndex = remember { mutableStateOf(0) }

    // Global lists
    val violentRecordsAll = remember { mutableStateOf(vm2.violentShakesAll) }
    val quickRecordsAll = remember { mutableStateOf(vm2.quickShakesAll) }
    val longRecordsAll = remember { mutableStateOf(vm2.longShakesAll) }

    // Friends lists
    val violentRecordsFriends = remember { mutableStateOf(vm2.violentShakesFriends) }
    val quickRecordsFriends = remember { mutableStateOf(vm2.quickShakesFriends) }
    val longRecordsFriends = remember { mutableStateOf(vm2.longShakesFriends) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        TabsView(selectedIndex = selectedIndexGroup, headers = groupHeaders)
        Spacer(modifier = Modifier.height(5.dp))
        //Text("Type", style = MaterialTheme.typography.h6)
        TabsView(selectedIndex = selectedShakeIndex, headers = shakeTypes)

        Spacer(Modifier.height(30.dp))
        Text(text = "Ranking:")

        Spacer(Modifier.height(10.dp))

        // Global lists
        if (selectedIndexGroup.value == 0) {
            UserList(viewModel = vm, allUsers)
            when (selectedShakeIndex.value) {
                0 -> UserList(viewModel = vm, userList = longRecordsAll)
                1 -> UserList(viewModel = vm, userList = violentRecordsAll)
                2 -> UserList(viewModel = vm, userList = quickRecordsAll)
            }
        }
        // Friend lists
        else {
            UserList(viewModel = vm, friendList)
            when (selectedShakeIndex.value) {
                0 -> UserList(viewModel = vm, userList = longRecordsFriends)
                1 -> UserList(viewModel = vm, userList = violentRecordsFriends)
                2 -> UserList(viewModel = vm, userList = quickRecordsFriends)
            }
        }
    }
}