package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.CREATE_SHAKE_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.LongShakeScreen
import org.sbma_shakeit.screens.QuickShakeScreen
import org.sbma_shakeit.screens.ViolentShakeScreen

fun NavGraphBuilder.createShakeNavGraph(
    navController: NavController
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
            LongShakeScreen(navController = navController)
        }

        // Violent Shake
        composable(
            route = Screen.ViolentShake.route
        ){
            ViolentShakeScreen(navController = navController)
        }
    }
}