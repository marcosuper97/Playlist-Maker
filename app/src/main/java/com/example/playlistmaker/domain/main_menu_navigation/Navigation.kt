package com.example.playlistmaker.domain.main_menu_navigation

sealed class Navigation {
    object Search : Navigation()
    object Library : Navigation()
    object Settings : Navigation()
}