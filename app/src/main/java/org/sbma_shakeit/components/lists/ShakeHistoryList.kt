package org.sbma_shakeit.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.ViewModelModule

@Composable
fun ShakeHistoryList(shakes: State<List<Shake>>, navController: NavController) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        item {
            HistoryListHeaders()
        }
        items(shakes.value) { shake ->
            HistoryListItem(shake, navController)
        }
    }
}

@Composable
private fun HistoryListHeaders() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Type")
        Text("Score/Time")
    }
}

@Composable
private fun HistoryListItem(shake: Shake, navController: NavController) {
    Card(
        Modifier.padding(vertical = 5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable {
                    val vm = ViewModelModule.provideShowShakeViewModel()
                    vm.setShake_(shake)
                    navController.navigate(Screen.Shake.route)
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(getTypeName(shake.type))
            if (shake.type == Shake.TYPE_LONG) {
                Text("${shake.duration / 1000} sec")
            } else {
                Text("${shake.score}")
            }
        }
    }
}

private fun getTypeName(type: Int): String =
    when (type) {
        Shake.TYPE_LONG -> "Long"
        Shake.TYPE_QUICK -> "Quick"
        Shake.TYPE_VIOLENT -> "Violent"
        else -> ""
    }