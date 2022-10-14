package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import org.sbma_shakeit.components.ShakeHistoryList
import org.sbma_shakeit.components.TabsView
import org.sbma_shakeit.viewmodels.HistoryViewModel

@Composable
fun HistoryScreen(
    navController: NavController
) {
    val vm = HistoryViewModel()
    val tabHeaders = listOf("All", "Long", "Violent", "Quick")
    val selectedIndex = remember { mutableStateOf(0) }

    val allShakes = remember { mutableStateOf(vm.allShakes) }
    val longShakes = remember { mutableStateOf(vm.longShakes) }
    val quickShakes = remember { mutableStateOf(vm.quickShakes) }
    val violentShakes = remember { mutableStateOf(vm.violentShakes) }

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




