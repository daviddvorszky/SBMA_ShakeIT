package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import org.sbma_shakeit.R
import org.sbma_shakeit.components.lists.ShakeHistoryList
import org.sbma_shakeit.components.TabsView
import org.sbma_shakeit.viewmodels.HistoryViewModel

/**
 * Shows users own shakes history
 * */
@Composable
fun HistoryScreen(
    navController: NavController
) {
    val historyViewModel = HistoryViewModel()
    val tabHeaders = listOf("${stringResource(R.string.all)}", "${stringResource(R.string.long_)}", "${stringResource(R.string.violent)}", "${stringResource(R.string.quick)}")
    val selectedIndex = remember { mutableStateOf(0) }

    val allShakes = remember { mutableStateOf(historyViewModel.allShakes) }
    val longShakes = remember { mutableStateOf(historyViewModel.longShakes) }
    val quickShakes = remember { mutableStateOf(historyViewModel.quickShakes) }
    val violentShakes = remember { mutableStateOf(historyViewModel.violentShakes) }

    Column {
        TabsView(selectedIndex = selectedIndex, headers = tabHeaders)
        when (selectedIndex.value) {
            0 -> ShakeHistoryList(shakes = allShakes, navController)
            1 -> ShakeHistoryList(shakes = longShakes, navController)
            2 -> ShakeHistoryList(shakes = violentShakes, navController)
            3 -> ShakeHistoryList(shakes = quickShakes, navController)
        }
    }
}




