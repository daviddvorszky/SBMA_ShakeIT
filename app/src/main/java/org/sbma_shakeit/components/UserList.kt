package org.sbma_shakeit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserList(viewModel: UserViewModel, userList: State<List<User>>) {

    //val allUsers = viewModel.getAll().observeAsState(listOf())

    LazyColumn {
        items(userList.value) { user ->
            Spacer(Modifier.height(10.dp))
//            Card(/*Modifier.padding(5.dp)*/) {
                UserListItem(itemUser = user, userViewModel = viewModel)
//            }
        }
    }
}