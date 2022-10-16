package org.sbma_shakeit.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.sbma_shakeit.R
import org.sbma_shakeit.components.TabsView
import org.sbma_shakeit.components.lists.UserList
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.viewmodels.ScoreboardViewModel
import org.sbma_shakeit.viewmodels.users.UserViewModel

/**
 * Screen that displays all users' and current user's friends record scores sorted from best to worst.
 * Lists are displayed by shake type.
 * */
@Composable
fun GlobalScoreboardScreen() {
    val userViewModel = UserViewModel(LocalContext.current.applicationContext as Application)
    val scoreboardViewModel = ScoreboardViewModel()

    // Tabs
    val groupHeaders = listOf(stringResource(R.string.global), stringResource(R.string.friends))
    val shakeTypes = listOf(
        stringResource(R.string.long_), stringResource(R.string.quick),
        stringResource(R.string.violent)
    )
    val selectedIndexGroup = remember { mutableStateOf(0) }
    val selectedShakeIndex = remember { mutableStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        TabsView(selectedIndex = selectedIndexGroup, headers = groupHeaders)
        Spacer(modifier = Modifier.height(5.dp))
        TabsView(selectedIndex = selectedShakeIndex, headers = shakeTypes)

        Spacer(Modifier.height(30.dp))
        Text(text = "${stringResource(R.string.ranking)}:")

        Spacer(Modifier.height(10.dp))

        if (selectedIndexGroup.value == 0) {
            ShowGlobalScoreList(
                scoreboardViewModel = scoreboardViewModel,
                userViewModel = userViewModel,
                selectedShakeIndex = selectedShakeIndex
            )
        } else {
            ShowFriendsScoreList(
                scoreboardViewModel = scoreboardViewModel,
                userViewModel = userViewModel,
                selectedShakeIndex = selectedShakeIndex
            )
        }
    }
}

@Composable
private fun ShowGlobalScoreList(
    scoreboardViewModel: ScoreboardViewModel,
    userViewModel: UserViewModel,
    selectedShakeIndex: State<Int>
) {
    val violentRecordsAll = remember { mutableStateOf(scoreboardViewModel.violentShakesAll) }
    val quickRecordsAll = remember { mutableStateOf(scoreboardViewModel.quickShakesAll) }
    val longRecordsAll = remember { mutableStateOf(scoreboardViewModel.longShakesAll) }
    when (selectedShakeIndex.value) {
        0 -> UserList(
            viewModel = userViewModel,
            usersWithShakes = longRecordsAll,
            shakeType = Shake.TYPE_LONG
        )
        1 -> UserList(
            viewModel = userViewModel,
            usersWithShakes = violentRecordsAll,
            shakeType = Shake.TYPE_VIOLENT
        )
        2 -> UserList(
            viewModel = userViewModel,
            usersWithShakes = quickRecordsAll,
            shakeType = Shake.TYPE_QUICK
        )
    }
}

@Composable
private fun ShowFriendsScoreList(
    scoreboardViewModel: ScoreboardViewModel,
    userViewModel: UserViewModel,
    selectedShakeIndex: State<Int>
) {
    val violentRecordsFriends =
        remember { mutableStateOf(scoreboardViewModel.violentShakesFriends) }
    val quickRecordsFriends = remember { mutableStateOf(scoreboardViewModel.quickShakesFriends) }
    val longRecordsFriends = remember { mutableStateOf(scoreboardViewModel.longShakesFriends) }

    when (selectedShakeIndex.value) {
        0 -> UserList(
            viewModel = userViewModel,
            usersWithShakes = longRecordsFriends,
            shakeType = Shake.TYPE_LONG
        )
        1 -> UserList(
            viewModel = userViewModel,
            usersWithShakes = violentRecordsFriends,
            shakeType = Shake.TYPE_VIOLENT
        )
        2 -> UserList(
            viewModel = userViewModel,
            usersWithShakes = quickRecordsFriends,
            shakeType = Shake.TYPE_QUICK
        )
    }
}