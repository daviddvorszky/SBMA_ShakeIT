package org.sbma_shakeit.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import org.sbma_shakeit.R

enum class FriendsButtonType { ADD, CANCEL, FRIEND }

/**
 * Button for friend related actions
 * */
@Composable
fun FriendsButton(
    type: FriendsButtonType,
    onClickAction: () -> Unit = {}
) {
    val buttonContent = ButtonContent(type)

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        onClick = onClickAction
    ) {
        Row {
            Text(buttonContent.text, fontWeight = FontWeight.Bold)
            Icon(
                painterResource(buttonContent.icon),
                contentDescription = buttonContent.contentDescription
            )
        }
    }
}

private class ButtonContent(type: FriendsButtonType) {
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