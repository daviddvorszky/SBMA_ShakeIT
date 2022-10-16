package org.sbma_shakeit.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.sbma_shakeit.R
import org.sbma_shakeit.data.room.User
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.ui.theme.Purple700
import org.sbma_shakeit.viewmodels.users.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val auth = authViewModel.auth.value
    val context = LocalContext.current
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val username = remember { mutableStateOf(TextFieldValue()) }
    var showPassword by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("${stringResource(R.string.back_to_login)}"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { navController.navigate(Screen.Login.route) },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple700
            )
        )
    }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${stringResource(R.string.register)}", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "${stringResource(R.string.username)}") },
            value = username.value,
            onValueChange = { username.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "${stringResource(R.string.email)}") },
            value = email.value,
            onValueChange = { email.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "${stringResource(R.string.password)}") },
            value = password.value,
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    if (showPassword)
                        Icon(
                            painterResource(R.drawable.visibility_off),
                            contentDescription = "${stringResource(R.string.dont_show_password)}"
                        )
                    else
                        Icon(
                            painterResource(R.drawable.visibility),
                            contentDescription = "${stringResource(R.string.show_password)}"
                        )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    auth ?: return@Button
                    val isUserNameTaken = authViewModel.isUsernameTaken(username.value.text)
                    Log.d("isUsernameTaken", "$isUserNameTaken")
                    if (isUserNameTaken) {
                        Toast.makeText(context, "${context.getString(R.string.username_taken)}", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    auth.createUserWithEmailAndPassword(email.value.text, password.value.text)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                authViewModel.createUser(User(username.value.text, email.value.text))
                                Log.d("REGISTER", "SUCCESS")
                                navController.navigate(Screen.NewShakeList.route)
                            } else {
                                val message = it.exception?.message ?: "${context.getString(R.string.register_failed)}"
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                Log.d("REGISTER", "FAILED", it.exception)
                            }
                        }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "${stringResource(R.string.sign_up_here)}")
            }
        }
    }
}