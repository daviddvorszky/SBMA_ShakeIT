package org.sbma_shakeit.navigation

const val ROOT_GRAPH_ROUTE = "root"
const val NEW_SHAKE_GRAPH_ROUTE = "new_shake"
const val CREATE_SHAKE_GRAPH_ROUTE = "create_shake"
const val HISTORY_GRAPH_ROUTE = "history"
const val SCOREBOARD_GRAPH_ROUTE = "scoreboard"
const val USER_GRAPH_ROUTE = "user"
const val AUTH_GRAPH_ROUTE = "auth"

sealed class Screen(val route: String){
    object NewShakeList : Screen(route = "new_shake_list_screen")
    object QuickShake : Screen(route = "quick_shake_screen")
    object LongShake : Screen(route = "long_shake_screen")
    object ViolentShake : Screen(route = "violent_shake_screen")
    object History : Screen(route = "history_screen")
    object GlobalScoreboard : Screen(route = "global_scoreboard_screen")
    object FriendsScoreboard : Screen(route = "friends_scoreboard_screen")
    object FriendList : Screen(route = "friend_list_screen")
    object UserProfile : Screen(route = "user_profile_screen")
    object Register : Screen(route = "register_screen")
    object Login : Screen(route = "login_screen")
    object Settings : Screen(route = "settings")
    object Shake : Screen(route = "shake")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}