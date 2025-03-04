package com.example.playlistmaker.domain.search

import android.content.SharedPreferences

interface StoreCleanerRepository {
    fun cleanStore(searchHistoryPreferences: SharedPreferences)
}