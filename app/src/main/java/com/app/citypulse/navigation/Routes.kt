package com.app.citypulse.navigation

object Graph{
    const val RootGraph = "rootGraph"
    const val AuthGraph = "authGraph"
    const val MainScreenGraph = "mainScreenGraph"
    const val ProfileScreenGraph = "profileScreenGraph"
    const val SettingsScreenGraph = "settingsScreenGraph"
    const val FriendsScreenGraph = "friendsScreenGraph"
    const val EventScreenGraph = "eventScreenGraph"
}

sealed class AuthRouteScreen(val route:String){

    object Login : AuthRouteScreen("login")
    object Register : AuthRouteScreen("register")
    object Register2 : AuthRouteScreen("register2")

}

sealed class MainRouteScreen(val route:String){

    object Map : MainRouteScreen("map_screen")
    object Profile : MainRouteScreen("profile")
    object Settings : MainRouteScreen("settings")
}

sealed class EventRouteScreen(val route:String){

    object ShowDetail : EventRouteScreen("event_details")
    object CreateEvent : EventRouteScreen("create_event")
    object AddPhotoEvent : EventRouteScreen("add_photos_event")

}

sealed class ProfileRouteScreen(val route:String){

    object AddFriend : ProfileRouteScreen("addfriend")
    object SavedEvents : ProfileRouteScreen("saved_events")
    object AssistedEvents : ProfileRouteScreen("assisted_events")



}