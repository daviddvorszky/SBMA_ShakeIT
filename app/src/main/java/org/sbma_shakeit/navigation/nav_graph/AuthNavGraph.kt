package org.sbma_shakeit.navigation.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.sbma_shakeit.navigation.AUTH_GRAPH_ROUTE
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.screens.LoginScreen
import org.sbma_shakeit.screens.RegisterScreen
import org.sbma_shakeit.viewmodels.AuthViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel
){
    navigation(
        startDestination = Screen.Login.route,
        route = AUTH_GRAPH_ROUTE
    ){
        // Login
        composable(
            route = Screen.Login.route
        ){
            LoginScreen(navController = navController,
                authViewModel = authViewModel)
        }
        
        // Register
        composable(
            route = Screen.Register.route
        ){
            RegisterScreen(navController = navController,
                authViewModel = authViewModel)
        }
    }
}