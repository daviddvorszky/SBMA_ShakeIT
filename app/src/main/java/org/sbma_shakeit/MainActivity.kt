package org.sbma_shakeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.sbma_shakeit.components.TopMenuBar
import org.sbma_shakeit.navigation.nav_graph.SetupNavGraph
import org.sbma_shakeit.ui.theme.SBMA_ShakeITTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SBMA_ShakeITTheme {
                navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopMenuBar(navController = navController, "New Shake")
                    }
                ) {
                    innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)){
                        SetupNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}