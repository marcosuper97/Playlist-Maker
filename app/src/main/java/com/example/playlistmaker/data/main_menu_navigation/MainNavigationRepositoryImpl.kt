package com.example.playlistmaker.data.main_menu_navigation

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationRepository
import com.example.playlistmaker.ui.library.fragments.MediaLibraryFragment
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.settings.fragment.SettingsFragment

class MainNavigationRepositoryImpl(private val context: Context):MainNavigationRepository {
    override fun library() {
        val libraryIntent = Intent(context, MediaLibraryFragment::class.java)
        context.startActivity(libraryIntent)
    }

    override fun search() {
        val searchIntent = Intent(context, SearchFragment::class.java)
        context.startActivity(searchIntent)
    }

    override fun settings() {
        val settingsIntent = Intent(context, SettingsFragment::class.java)
        context.startActivity(settingsIntent)
    }
}