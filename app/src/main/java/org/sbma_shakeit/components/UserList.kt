package org.sbma_shakeit.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserList(viewModel: UserViewModel, userList: State<List<User>>) {

    LazyColumn {
        items(userList.value) { user ->
            Spacer(Modifier.height(10.dp))
            UserListItem(itemUser = user, userViewModel = viewModel)
        }
    }
}