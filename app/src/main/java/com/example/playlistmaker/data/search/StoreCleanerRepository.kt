package com.example.playlistmaker.data.search

import android.content.SharedPreferences

interface StoreCleanerRepository {
    fun cleanStore(searchHistoryPreferences: SharedPreferences)
}