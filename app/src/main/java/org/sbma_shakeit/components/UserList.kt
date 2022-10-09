package org.sbma_shakeit.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import org.sbma_shakeit.viewmodels.UserViewModel

@Composable
fun UserList(viewModel: UserViewModel) {

    val allUsers = viewModel.allUsers

    val test = viewModel.getAll().observeAsState(listOf())
    Log.d("TESTI----", test.value.toString())

    LazyColumn {
        item {
            Text(test.value.size.toString())
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { /*viewModel.orderUsersByViolent() */}) {
                    Text("By violent")
                }
                Button(onClick = { /*viewModel.orderUsersByQuick()*/ }) {
                    Text("By Quick")
                }
                Button(onClick = { /*viewModel.orderUsersByLong()*/ }) {
                    Text("By Long")
                }
            }
        }
        items(/*allUsers*/test.value) { user ->
            UserListItem(itemUser = user, userViewModel = viewModel)
        }
    }
}