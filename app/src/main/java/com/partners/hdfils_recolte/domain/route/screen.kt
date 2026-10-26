package com.partners.hdfils_recolte.domain.route

enum class ScreenRoute {
    Auth,
    Register,
    Home,
}

fun startRoute(loggedIn: Boolean): ScreenRoute {
    return if (loggedIn) ScreenRoute.Home else ScreenRoute.Auth
}
