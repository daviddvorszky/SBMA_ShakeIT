package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    vm: UserViewModel = viewModel()
) {
    val user = vm.getCurrentUser().observeAsState()
    val userData = user.value ?: return
//    val date = userData.quickShake.created
//    val f = SimpleDateFormat("dd.MM.yyyy").format(date)

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            elevation = 10.dp,
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize(0.95f)
        ) {
            Text(userData.username, fontWeight = FontWeight.Bold)

        }
    }
}

//        Text("date $f")
//        Text("Long shake time ${userData.longShake.time}s")
//        Text("Violent shake score ${userData.violentShake.score}")
//        Text("Quick shake score ${userData.quickShake.score}")
//            FriendRequestList()