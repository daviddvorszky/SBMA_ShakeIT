package org.sbma_shakeit.navigation.nav_graph

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.navigation.AUTH_GRAPH_ROUTE
import org.sbma_shakeit.navigation.NEW_SHAKE_GRAPH_ROUTE
import org.sbma_shakeit.navigation.ROOT_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.SettingsScreen
import org.sbma_shakeit.screens.ShakeScreen
import org.sbma_shakeit.viewmodels.SingleShakeViewModel
import org.sbma_shakeit.viewmodels.users.AuthViewModel
import java.io.File

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    application: Application,
    database: ShakeItDB,
    outputDirectory: File,
    fusedLocationClient: FusedLocationProviderClient,
    showShakeViewModel: SingleShakeViewModel
) {
    val startDestination =
        if (Firebase.auth.currentUser != null) NEW_SHAKE_GRAPH_ROUTE
        else AUTH_GRAPH_ROUTE

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = ROOT_GRAPH_ROUTE
    ) {
        newShakeNavGraph(
            navController,
            application,
            database,
            outputDirectory,
            fusedLocationClient
        )
        historyNavGraph(navController = navController)
        scoreboardNavGraph()
        userNavGraph()
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

        composable(
            route = Screen.Shake.route
        ) {
            ShakeScreen(showShakeViewModel)
        }
    }
}