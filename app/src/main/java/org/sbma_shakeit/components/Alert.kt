package org.sbma_shakeit.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.sbma_shakeit.R

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
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                onClick = dismissAction
            ) {
                Text(stringResource(R.string.no))
            }
        }

    )
}