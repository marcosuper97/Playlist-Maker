package com.example.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.StoreCleanerRepository
import com.example.playlistmaker.domain.search.StoreCleanerInterractor

class StorageCleanerInterractorImpl(private val storeCleanerRepository: StoreCleanerRepository):
    StoreCleanerInterractor {
    override fun execute(sharedPreferences: SharedPreferences) {
        storeCleanerRepository.cleanStore(sharedPreferences)
    }
}