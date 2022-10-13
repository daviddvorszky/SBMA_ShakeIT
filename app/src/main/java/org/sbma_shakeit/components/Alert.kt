package org.sbma_shakeit.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable

@Composable
fun MyAlert(
    title: String,
    text: String,
    confirmAction: () -> Unit = {},
    dismissAction: () -> Unit = {}
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Text(text)
        },
        onDismissRequest = dismissAction,
        confirmButton = {
            Button(
                colors = ButtonDefaults
                    .buttonColors(backgroundColor = MaterialTheme.colors.primary),
                onClick = confirmAction
            ) {
                Text("YES")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                onClick = dismissAction
            ) {
                Text("NO")
            }
        }

    )
}