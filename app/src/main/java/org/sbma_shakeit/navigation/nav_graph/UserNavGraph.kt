package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.navigation.USER_GRAPH_ROUTE
import org.sbma_shakeit.screens.FriendsScreen
import org.sbma_shakeit.screens.UserProfileScreen

fun NavGraphBuilder.userNavGraph(){
    navigation(
        startDestination = Screen.UserProfile.route,
        route = USER_GRAPH_ROUTE
    ){
        // User Profile
        composable(
            route = Screen.UserProfile.route
        ){
            UserProfileScreen()
        }
        
        // Friend List
        composable(
            route = Screen.FriendList.route
        ){
            FriendsScreen()
        }
    }
}