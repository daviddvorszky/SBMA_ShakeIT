package org.sbma_shakeit.navigation.nav_graph

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.android.gms.location.FusedLocationProviderClient
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.navigation.NEW_SHAKE_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.NewShakeListScreen
import java.io.File

fun NavGraphBuilder.newShakeNavGraph(
    navController: NavController,
    application: Application,
    database: ShakeItDB,
    fusedLocationClient: FusedLocationProviderClient,
    outputDirectory: File,
){
    navigation(
        startDestination = Screen.NewShakeList.route,
        route = NEW_SHAKE_GRAPH_ROUTE
    ){
        createShakeNavGraph(
            application,
            database,
            fusedLocationClient,
            outputDirectory
        )

        // New Shake List / Main
        composable(
            route = Screen.NewShakeList.route
        ){
            NewShakeListScreen(navController = navController)
        }
    }
}