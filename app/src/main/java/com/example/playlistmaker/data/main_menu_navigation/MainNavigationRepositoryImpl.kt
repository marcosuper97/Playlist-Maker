package com.example.playlistmaker.data.main_menu_navigation

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationRepository
import com.example.playlistmaker.ui.library.activity.LibraryActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainNavigationRepositoryImpl(private val context: Context):MainNavigationRepository {
    override fun library() {
        val libraryIntent = Intent(context, LibraryActivity::class.java)
        context.startActivity(libraryIntent)
    }

    override fun search() {
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    override fun settings() {
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsIntent)
    }
}