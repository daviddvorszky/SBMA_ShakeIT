package org.sbma_shakeit.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.sbma_shakeit.R

@Composable
fun LoginScreen(
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column {
        TextField(
            label = { Text("Username") },
            value = username,
            onValueChange = { username = it })
        Row {
            TextField(
                label = { Text("Password") },
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        showPassword = !showPassword
                    }) {
                        if (showPassword)
                            Icon(
                                painterResource(R.drawable.visibility_off),
                                contentDescription = "Show password")
                        else
                            Icon(
                                painterResource(R.drawable.visibility),
                                contentDescription = "Show password")
                    }
                }
            )
        }
        Button(onClick = { /*TODO: Action for logging in*/ }) {
            Text("Sign in")
        }
    }
}

//@Preview
//@Composable
//fun ComposablePreview() {
//    LoginScreen(navController = NavController(LocalContext.current))
//}