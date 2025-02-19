package com.example.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.StoreGetSetRepositoryImpl.Companion
import com.example.playlistmaker.domain.api.StoreCleanerRepository

class StoreCleanerRepositoryImpl(private val searchHistoryPreferences: SharedPreferences):StoreCleanerRepository {

    override fun cleanStore() {
        searchHistoryPreferences.edit().clear().apply()
    }

}