package com.example.playlistmaker.domain.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.StoreCleanerInterractor
import com.example.playlistmaker.domain.search.StoreCleanerRepository

class StorageCleanerInterractorImpl(private val storeCleanerRepository: StoreCleanerRepository):
    StoreCleanerInterractor {
    override fun execute(sharedPreferences: SharedPreferences) {
        storeCleanerRepository.cleanStore(sharedPreferences)
    }
}