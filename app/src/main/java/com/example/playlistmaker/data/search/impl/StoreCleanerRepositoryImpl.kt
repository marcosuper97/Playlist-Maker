package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.StoreCleanerRepository

class StoreCleanerRepositoryImpl() :
    StoreCleanerRepository {

    override fun cleanStore(searchHistoryPreferences: SharedPreferences) {
        searchHistoryPreferences.edit().clear().apply()
    }

}