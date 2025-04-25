package com.partners.hdfils_recolte.domain.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.partners.hdfils_recolte.presentation.ui.pages.AuthPage
import com.partners.hdfils_recolte.presentation.ui.pages.HomePage


@Composable
fun Navigation(navC: NavHostController, isConnected: Boolean){
    NavHost(navController = navC, startDestination = ScreenRoute.Auth.name, route = "root") {
        composable(ScreenRoute.Auth.name) {
            AuthPage(navC,isConnected)
        }
        composable(ScreenRoute.Home.name) {
            HomePage(navC,isConnected)
        }
    }

}