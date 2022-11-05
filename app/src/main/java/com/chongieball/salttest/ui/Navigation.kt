package com.chongieball.salttest.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chongieball.salttest.ui.screen.home.HomeScreen
import com.chongieball.salttest.ui.screen.login.LoginScreen
import org.koin.androidx.compose.koinViewModel

object Route {
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun MyNavigation(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Route.LOGIN) {
        composable(Route.LOGIN) { LoginScreen(loginViewModel = koinViewModel(), navHostController = navHostController)}
        composable("${Route.HOME}/{userId}", arguments = listOf(
            navArgument("userId") { type = NavType.IntType }
        )) {
            val userId = it.arguments?.getInt("userId") ?: return@composable
            HomeScreen(userId = userId, homeViewModel = koinViewModel())
        }
    }
}