package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sbma_shakeit.MainActivity
import org.sbma_shakeit.components.lists.FriendRequestList
import org.sbma_shakeit.components.UserListItem
import org.sbma_shakeit.R
import org.sbma_shakeit.viewmodels.users.UserViewModel
// TODO:CLEAN
@Composable
fun FriendListScreen(){
    val vm: UserViewModel = viewModel()
    // TODO: Get friends from room
    val friendList = vm.friends
    val friendReqs = vm.friendRequests

    var selectedIndex by remember { mutableStateOf(0) }
    val list = listOf(stringResource(R.string.friends), stringResource(R.string.requests))

    Column {
        TabRow(selectedTabIndex = selectedIndex, backgroundColor = Color.Transparent) {
            list.forEachIndexed { index, text ->
                val selected = selectedIndex == index
                Tab(
                    modifier = if (selected) Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(MaterialTheme.colors.primaryVariant)
                        .height(50.dp)
                    else Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(Color.White)
                        .height(50.dp),
                    selected = selected,
                    onClick = { selectedIndex = index },
                    selectedContentColor = /*Color.Black*/
                        if (MainActivity.isDarkMode.value) MaterialTheme.colors.secondary
                            else MaterialTheme.colors.primaryVariant,
                    unselectedContentColor =
                        if (MainActivity.isDarkMode.value) Color.White else Color.Gray
                ) {
                    if (index == 0)
                        Text("$text(${friendList.size})", fontWeight = FontWeight.Bold)
                    else
                        Text("$text(${friendReqs.size})", fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Column(Modifier.padding(10.dp)) {
            if (selectedIndex == 0) {
                FriendList()
            } else {
                FriendRequestList()
            }
        }

    }
}

@Composable
fun FriendList(vm: UserViewModel = viewModel()) {
    val friendList = vm.friends
    LazyColumn {
        items(friendList) { friend ->
            UserListItem(itemUser = friend, userViewModel = vm)
        }
    }

}