package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.StoreCleanerInterractor
import com.example.playlistmaker.domain.api.StoreCleanerRepository

class StorageCleanerInterractorImpl(private val storeCleanerRepository: StoreCleanerRepository): StoreCleanerInterractor {
    override fun execute() {
        storeCleanerRepository.cleanStore()
    }
}