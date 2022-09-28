package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.navigation.USER_GRAPH_ROUTE
import org.sbma_shakeit.screens.LoginScreen
import org.sbma_shakeit.screens.RegisterScreen

fun NavGraphBuilder.userNavGraph(
    navController: NavController
){
    navigation(
        startDestination = Screen.UserProfile.route,
        route = USER_GRAPH_ROUTE
    ){
        // User Profile
        composable(
            route = Screen.UserProfile.route
        ){
            LoginScreen(navController = navController)
        }
        
        // Friend List
        composable(
            route = Screen.FriendList.route
        ){
            RegisterScreen(navController = navController)
        }
    }
}