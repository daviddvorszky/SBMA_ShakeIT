package org.sbma_shakeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.sbma_shakeit.components.TopMenuBar
import org.sbma_shakeit.navigation.nav_graph.SetupNavGraph
import org.sbma_shakeit.ui.theme.SBMA_ShakeITTheme
import org.sbma_shakeit.viewmodels.AuthViewModel
import org.sbma_shakeit.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = AuthViewModel()
        authViewModel.auth.value = Firebase.auth
        setContent {
            SBMA_ShakeITTheme {
                navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopMenuBar(navController = navController, "New Shake")
                    }
                ) {
                    innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        SetupNavGraph(navController = navController,
                        authViewModel = authViewModel)
                    }
                }
            }
        }
    }
}