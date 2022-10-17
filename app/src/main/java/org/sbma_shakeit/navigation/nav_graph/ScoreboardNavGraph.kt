package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.SCOREBOARD_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.ScoreboardScreen

fun NavGraphBuilder.scoreboardNavGraph(){
    navigation(
        startDestination = Screen.Scoreboard.route,
        route = SCOREBOARD_GRAPH_ROUTE
    ){
        // Global Scoreboard
        composable(
            route = Screen.Scoreboard.route
        ){
            ScoreboardScreen()
        }
    }
}