package com.partners.hdfils_recolte.domain.route

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.partners.hdfils_recolte.presentation.ui.pages.AuthPage
import com.partners.hdfils_recolte.presentation.ui.pages.HomePage
import com.partners.hdfils_recolte.presentation.ui.pages.RegisterPage

@Composable
fun Navigation(
    navC: NavHostController,
    isConnected: Boolean,
    startDestination: String = ScreenRoute.Auth.name,
) {
    NavHost(
        navController = navC,
        startDestination = startDestination,
        route = "root",
        enterTransition = {
            fadeIn(tween(320)) + slideInHorizontally(
                animationSpec = tween(420, easing = FastOutSlowInEasing),
                initialOffsetX = { it / 5 },
            )
        },
        exitTransition = { fadeOut(tween(180)) },
        popEnterTransition = { fadeIn(tween(280)) },
        popExitTransition = {
            fadeOut(tween(180)) + slideOutHorizontally(
                animationSpec = tween(320, easing = FastOutSlowInEasing),
                targetOffsetX = { it / 5 },
            )
        },
    ) {
        composable(ScreenRoute.Auth.name) {
            AuthPage(navC, isConnected)
        }
        composable(ScreenRoute.Register.name) {
            RegisterPage(navC, isConnected)
        }
        composable(ScreenRoute.Home.name) {
            HomePage(navC, isConnected)
        }
    }
}
