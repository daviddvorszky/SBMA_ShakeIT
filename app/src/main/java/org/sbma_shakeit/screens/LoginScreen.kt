package org.sbma_shakeit.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import org.sbma_shakeit.R
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val auth = authViewModel.auth.value
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("To register")
        }
        TextField(
            label = { Text("Email") },
            value = email,
            onValueChange = { email = it })

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
                            contentDescription = "Don't show password"
                        )
                    else
                        Icon(
                            painterResource(R.drawable.visibility),
                            contentDescription = "Show password"
                        )
                }
            }
        )

        Button(onClick = {
            auth ?: return@Button
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("LOGIN", "SUCCESS")
                        navController.navigate(Screen.NewShakeList.route)
                    } else {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                        Log.d("LOGIN", "FAILED", it.exception)
                    }
                }
        }) {
            Text("Sign in")
        }
    }
}

//@Preview
//@Composable
//fun ComposablePreview() {
//    LoginScreen(navController = NavController(LocalContext.current))
//}