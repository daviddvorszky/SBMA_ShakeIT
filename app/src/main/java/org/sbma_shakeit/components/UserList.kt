package org.sbma_shakeit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun UserList(viewModel: UserViewModel) {

    val allUsers = viewModel.getAll().observeAsState(listOf())

    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /*viewModel.orderUsersByViolent() */ }) {
                    Text("Violent")
                }
                Button(onClick = { /*viewModel.orderUsersByQuick()*/ }
                ) {
                    Text("Quick", fontSize = 10.sp)
                }
                Button(onClick = { /*viewModel.orderUsersByLong()*/ }) {
                    Text("Long")
                }
            }
        }
        items(allUsers.value) { user ->
            UserListItem(itemUser = user, userViewModel = viewModel)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}