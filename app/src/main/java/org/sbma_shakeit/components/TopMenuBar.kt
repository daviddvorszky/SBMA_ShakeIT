package org.sbma_shakeit.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.sbma_shakeit.navigation.Screen

var _title = mutableStateOf("asd")

@Composable
fun TopMenuBar(navController: NavController, title: String) {
    _title.value = title
    TopAppBar(
        title = {
            Text(_title.value)
        },
        actions = {
            TopMenuBarDropdown(navController)
        }
    )

}

@Composable
fun TopMenuBarDropdown(navController: NavController) {
    val expanded = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = {
            expanded.value = true
        }) {
            Icon(Icons.Default.Menu, "")
        }
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = {
            expanded.value = false
        }) {
        DropdownMenuItem(onClick = {
            expanded.value = false
            _title.value = "New Shake"
            navController.popBackStack()
            navController.navigate(Screen.NewShakeList.route)
        }) {
            Text("New Shake")
        }
        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            _title.value = "History"
            navController.popBackStack()
            navController.navigate(Screen.History.route)
        }) {
            Text("History")
        }
        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            _title.value = "Scoreboard"
            navController.popBackStack()
            navController.navigate(Screen.GlobalScoreboard.route)
        }) {
            Text("Scoreboard")
        }
        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            _title.value = "Friends"
            navController.popBackStack()
            navController.navigate(Screen.FriendList.route)
        }) {
            Text("Friends")
        }
        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            _title.value = "Profile"
            navController.popBackStack()
            navController.navigate(Screen.UserProfile.route)
        }) {
            Text("Profile")
        }
        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            _title.value = "Settings"
            navController.popBackStack()
            navController.navigate(Screen.Settings.route)
        }) {
            Text("Settings")
        }
        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
        }) {
            Text("Logout")
        }
    }
}