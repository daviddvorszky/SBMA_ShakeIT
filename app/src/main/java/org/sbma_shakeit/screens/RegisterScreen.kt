package org.sbma_shakeit.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import org.sbma_shakeit.R
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val auth = authViewModel.auth.value
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("To login")
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
                            contentDescription = "Show password"
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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("REGISTER", "SUCCESS")
                        navController.navigate(Screen.NewShakeList.route)
                    } else {
                        Toast.makeText(context, "Register failed", Toast.LENGTH_SHORT).show()
                        Log.d("REGISTER", "FAILED", it.exception)
                    }
                }
        }) {
            Text("Sign Up")
        }
    }
}