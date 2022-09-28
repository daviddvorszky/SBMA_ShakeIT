package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.NEW_SHAKE_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.NewShakeListScreen

fun NavGraphBuilder.newShakeNavGraph(
    navController: NavController
){
    navigation(
        startDestination = Screen.NewShakeList.route,
        route = NEW_SHAKE_GRAPH_ROUTE
    ){
        createShakeNavGraph(navController = navController)

        // New Shake List / Main
        composable(
            route = Screen.NewShakeList.route
        ){
            NewShakeListScreen(navController = navController)
        }
    }
}