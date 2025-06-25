package com.example.playlistmaker.domain.shairing.settings.impl

import com.example.playlistmaker.domain.shairing.settings.SharingInteractor
import com.example.playlistmaker.domain.shairing.settings.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun openSupport() {
        sharingRepository.openSupport()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }

    override fun shareApp() {
        sharingRepository.shareApp()
    }
}