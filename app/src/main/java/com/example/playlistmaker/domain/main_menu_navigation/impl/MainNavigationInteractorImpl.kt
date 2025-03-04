package com.example.playlistmaker.domain.main_menu_navigation.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationInteractor
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainNavigationInteractorImpl : MainNavigationInteractor {
    override fun library(context: Context) {
        val libraryIntent = Intent(context, LibraryActivity::class.java)
        context.startActivity(libraryIntent)
    }

    override fun search(context: Context) {
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    override fun settings(context: Context) {
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsIntent)
    }
}