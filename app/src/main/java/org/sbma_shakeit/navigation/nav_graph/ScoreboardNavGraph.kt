package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.SCOREBOARD_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.GlobalScoreboardScreen

fun NavGraphBuilder.scoreboardNavGraph(){
    navigation(
        startDestination = Screen.GlobalScoreboard.route,
        route = SCOREBOARD_GRAPH_ROUTE
    ){
        // Global Scoreboard
        composable(
            route = Screen.GlobalScoreboard.route
        ){
            GlobalScoreboardScreen()
        }
    }
}