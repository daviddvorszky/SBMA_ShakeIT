package org.sbma_shakeit.navigation.nav_graph

import android.app.Activity
import android.app.Application
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
            QuickShakeScreen(
                ViewModelModule.provideQuickShakeViewModel(application, activity, database)
            )
        }

        // Long Shake
        composable(
            route = Screen.LongShake.route
        ){
            LongShakeScreen(
                ViewModelModule.provideLongShakeViewModel(application, activity, database)
            )
        }

        // Violent Shake
        composable(
            route = Screen.ViolentShake.route
        ){
            ViolentShakeScreen(
                ViewModelModule.provideViolentShakeViewModel(application, activity, database)
            )
        }
    }
}