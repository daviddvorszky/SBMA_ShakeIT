package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.HISTORY_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.HistoryScreen

fun NavGraphBuilder.historyNavGraph(
    navController: NavController
){
    navigation(
        startDestination = Screen.History.route,
        route = HISTORY_GRAPH_ROUTE
    ){
        composable(
            Screen.History.route
        ){
            HistoryScreen(navController = navController)
        }
    }
}