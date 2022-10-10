package org.sbma_shakeit.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import org.sbma_shakeit.R

enum class FriendsButtonType { ADD, CANCEL, FRIEND }

@Composable
fun FriendsButton(type: FriendsButtonType, onClickAction: () -> Unit = {}) {

    val buttonContent = ButtonContent(type)

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonContent.backgroundColor),
        onClick = onClickAction
    ) {
        Row {
            Text(buttonContent.text)
            Icon(
                painterResource(buttonContent.icon),
                contentDescription = buttonContent.contentDescription
            )
        }
    }
}

private class ButtonContent(type: FriendsButtonType) {
    val backgroundColor = when (type) {
        FriendsButtonType.ADD -> Color.White
        FriendsButtonType.CANCEL -> Color.Red
        FriendsButtonType.FRIEND -> Color.Blue
    }
    val text = when (type) {
        FriendsButtonType.ADD -> "Add"
        FriendsButtonType.CANCEL -> "Cancel"
        FriendsButtonType.FRIEND -> "Friend"
    }
    val contentDescription = when (type) {
        FriendsButtonType.ADD -> "Send friend request"
        FriendsButtonType.CANCEL -> "Cancel friend request"
        FriendsButtonType.FRIEND -> "Remove from friends"
    }
    val icon = when (type) {
        FriendsButtonType.ADD -> R.drawable.friend_add
        FriendsButtonType.CANCEL -> R.drawable.remove_request
        FriendsButtonType.FRIEND -> R.drawable.check
    }
}