package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.StoreCleanerRepository

class StoreCleanerRepositoryImpl() :
    StoreCleanerRepository {

    override fun cleanStore(searchHistoryPreferences: SharedPreferences) {
        searchHistoryPreferences.edit().clear().apply()
    }

}