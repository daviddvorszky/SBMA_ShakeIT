package org.sbma_shakeit.components.lists

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sbma_shakeit.components.UserListItem
import org.sbma_shakeit.data.room.Shake
import org.sbma_shakeit.viewmodels.UserWithShake
import org.sbma_shakeit.viewmodels.users.UserViewModel

@Composable
fun UserList(
    viewModel: UserViewModel,
    userList: State<List<UserWithShake>>,
    shakeType: Int
) {
    LazyColumn {
        items(userList.value) { user ->
            Spacer(Modifier.height(10.dp))
            when (shakeType) {
                Shake.TYPE_LONG ->
                    UserListItem(
                        itemUser = user.user,
                        userViewModel = viewModel,
                        shake = user.long
                    )
                Shake.TYPE_VIOLENT ->
                    UserListItem(
                        itemUser = user.user,
                        userViewModel = viewModel,
                        shake = user.violent
                    )
                Shake.TYPE_QUICK ->
                    UserListItem(
                        itemUser = user.user,
                        userViewModel = viewModel,
                        shake = user.quick
                    )
            }
        }
    }
}