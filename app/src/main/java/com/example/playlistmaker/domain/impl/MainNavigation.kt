package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.api.AppNavigation
import com.example.playlistmaker.presentation.LibraryActivity
import com.example.playlistmaker.presentation.SearchActivity
import com.example.playlistmaker.presentation.SettingsActivity

class MainNavigation : AppNavigation {
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