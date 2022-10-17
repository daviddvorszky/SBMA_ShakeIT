package org.sbma_shakeit

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.sbma_shakeit.components.topMenuBar.AppBar
import org.sbma_shakeit.components.topMenuBar.DrawerBody
import org.sbma_shakeit.components.topMenuBar.DrawerHeader
import org.sbma_shakeit.components.topMenuBar.MenuItem
import org.sbma_shakeit.data.room.ShakeItDB
import org.sbma_shakeit.navigation.Screen
import org.sbma_shakeit.navigation.nav_graph.SetupNavGraph
import org.sbma_shakeit.ui.theme.Green200
import org.sbma_shakeit.ui.theme.Green500
import org.sbma_shakeit.ui.theme.SBMA_ShakeITTheme
import org.sbma_shakeit.viewmodels.ViewModelModule
import org.sbma_shakeit.viewmodels.users.AuthViewModel
import java.io.File

class MainActivity : ComponentActivity() {

    companion object {
        val isDarkMode = mutableStateOf(false)
        lateinit var lm: LocationManager
    }

    private lateinit var navController: NavHostController
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        hasLocationPermission()

        //Osmandroid conf
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        //

        requestLocationPermission()
        requestCameraPermission()

        val authViewModel = AuthViewModel(application)
        authViewModel.auth.value = Firebase.auth
        val context = this

        getSharedPrefs()

        val database = ShakeItDB.get(context)

        setContent {
            SBMA_ShakeITTheme(darkTheme = isDarkMode.value) {
                navController = rememberNavController()
                //MODIFIED
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()

                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        if (isDarkMode.value) Green200 else Green500)
                }
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            onNavigationIconClick = {
                                scope.launch {
                                    //if (Firebase.auth.currentUser != null)    Decomment to enable secure sign in
                                        scaffoldState.drawerState.open()
                                }
                            }
                        )
                    },
                    drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                    drawerContent = {
                        DrawerHeader()
                        DrawerBody(
                            items = listOf(
                                MenuItem(
                                    id = "home_page",
                                    title = "Home Page",
                                    contentDescription = "Go to new shake screen",
                                    icon = Icons.Default.Home
                                ),
                                MenuItem(
                                    id = "history",
                                    title = "History",
                                    contentDescription = "Go to history screen",
                                    icon = Icons.Default.List
                                ),
                                MenuItem(
                                    id = "scoreboard",
                                    title = "Scoreboard",
                                    contentDescription = "Go to scoreboard screen",
                                    icon = Icons.Default.Star
                                ),
                                MenuItem(
                                    id = "friends",
                                    title = "Friends",
                                    contentDescription = "Go to friends screen",
                                    icon = Icons.Default.Face
                                ),
                                MenuItem(
                                    id = "profile",
                                    title = "Profile",
                                    contentDescription = "Go to profile screen",
                                    icon = Icons.Default.Person
                                ),
                                MenuItem(
                                    id = "settings",
                                    title = "Settings",
                                    contentDescription = "Go to settings screen",
                                    icon = Icons.Default.Settings
                                ),
                                MenuItem(
                                    id = "logout",
                                    title = "Logout",
                                    contentDescription = "Go to logout screen",
                                    icon = Icons.Default.Lock
                                ),
                            ),
                            onItemClick = {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }

                                navController.popBackStack()

                                var route = ""
                                when(it.id){
                                    "home_page" -> route = Screen.NewShakeList.route
                                    "history"   -> route = Screen.History.route
                                    "scoreboard"-> route = Screen.Scoreboard.route
                                    "friends"   -> route = Screen.FriendList.route
                                    "profile"   -> route = Screen.UserProfile.route
                                    "settings"  -> route = Screen.Settings.route
                                    "logout"    -> {
                                        route = Screen.Login.route
                                        Firebase.auth.signOut()
                                        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                if (route != "")
                                    navController.navigate(route)
                            }
                        )
                    }
                ) {
                        innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SetupNavGraph(
                            navController = navController,
                            authViewModel = authViewModel,
                            application,
                            database,
                            LocationServices.getFusedLocationProviderClient(applicationContext),
                            getOutputDirectory(),
                            ViewModelModule.provideShowShakeViewModel()
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPref.edit().apply {
            putBoolean("isDarkMode", isDarkMode.value)
            apply()
        }
    }

    private fun getSharedPrefs() {
        sharedPref = getSharedPreferences("pref", MODE_PRIVATE)
        isDarkMode.value = sharedPref.getBoolean("isDarkMode", false)
    }

    private fun hasLocationPermission(): Boolean{//check permission to scan
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),0)
            return false
        }
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),0)
            return false
        }
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 123
        )
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA), 124
        )
    }

    private fun getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(it, this.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else this.filesDir
    }
}
