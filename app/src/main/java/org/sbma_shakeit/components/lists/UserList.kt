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
    usersWithShakes: State<List<UserWithShake>>,
    shakeType: Int
) {
    LazyColumn {
        items(usersWithShakes.value) { userWithShake ->
            Spacer(Modifier.height(10.dp))
            when (shakeType) {
                Shake.TYPE_LONG ->
                    UserListItem(
                        itemUser = userWithShake.user,
                        userViewModel = viewModel,
                        shake = userWithShake.long
                    )
                Shake.TYPE_VIOLENT ->
                    UserListItem(
                        itemUser = userWithShake.user,
                        userViewModel = viewModel,
                        shake = userWithShake.violent
                    )
                Shake.TYPE_QUICK ->
                    UserListItem(
                        itemUser = userWithShake.user,
                        userViewModel = viewModel,
                        shake = userWithShake.quick
                    )
            }
        }
    }
}