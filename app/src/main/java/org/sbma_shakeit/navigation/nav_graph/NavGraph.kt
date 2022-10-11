package org.sbma_shakeit.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.sbma_shakeit.navigation.AUTH_GRAPH_ROUTE
import org.sbma_shakeit.navigation.NEW_SHAKE_GRAPH_ROUTE
import org.sbma_shakeit.navigation.ROOT_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.SettingsScreen
import org.sbma_shakeit.viewmodels.users.AuthViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val startDestination =
        if (Firebase.auth.currentUser != null) NEW_SHAKE_GRAPH_ROUTE
        else AUTH_GRAPH_ROUTE

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = ROOT_GRAPH_ROUTE
    ) {
        newShakeNavGraph(navController = navController)
        historyNavGraph(navController = navController)
        scoreboardNavGraph(navController = navController)
        userNavGraph(navController = navController)
        authNavGraph(
            navController = navController,
            authViewModel = authViewModel
        )

        // Settings
        composable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }
    }
}