package org.sbma_shakeit.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
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
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            Button(onClick = confirmAction) {
                Text("YES")
            }
        },
        dismissButton = {
            Button(onClick = dismissAction) {
                Text("NO")
            }
        }

    )
}