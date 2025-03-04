package com.example.playlistmaker.domain.main_menu_navigation

import android.content.Context

interface MainNavigationInteractor {
    fun settings(context: Context)
    fun library(context: Context)
    fun search(context: Context)
}