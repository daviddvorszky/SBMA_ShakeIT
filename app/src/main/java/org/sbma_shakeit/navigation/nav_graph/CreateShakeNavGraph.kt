package org.sbma_shakeit.navigation.nav_graph

import android.app.Activity
import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.navigation.CREATE_SHAKE_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.LongShakeScreen
import org.sbma_shakeit.screens.QuickShakeScreen
import org.sbma_shakeit.screens.ViolentShakeScreen
import org.sbma_shakeit.viewmodels.ViewModelModule

fun NavGraphBuilder.createShakeNavGraph(
    navController: NavController,
    application: Application,
    activity: Activity,
    database: ShakeItDB
){
    navigation(
        startDestination = Screen.QuickShake.route,
        route = CREATE_SHAKE_GRAPH_ROUTE
    ){
        // Quick Shake
        composable(
            route = Screen.QuickShake.route
        ){
            QuickShakeScreen(navController = navController)
        }

        // Long Shake
        composable(
            route = Screen.LongShake.route
        ){
            LongShakeScreen(
                navController = navController,
                ViewModelModule.provideLongShakeViewModel(application, activity, database)
            )
        }

        // Violent Shake
        composable(
            route = Screen.ViolentShake.route
        ){
            ViolentShakeScreen(navController = navController)
        }
    }
}