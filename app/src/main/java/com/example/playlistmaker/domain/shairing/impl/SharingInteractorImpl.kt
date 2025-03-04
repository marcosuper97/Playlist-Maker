package com.example.playlistmaker.domain.shairing.impl

import android.content.Context
import com.example.playlistmaker.data.sharing.SharingRepository
import com.example.playlistmaker.domain.shairing.SharingInteractor

class SharingInteractorImpl(private val sharingRepository: SharingRepository):SharingInteractor {
    override fun openSupport(context: Context) {
        sharingRepository.openSupport(context)
    }

    override fun openTerms(context: Context) {
        sharingRepository.openTerms(context)
    }

    override fun shareApp(context: Context) {
        sharingRepository.shareApp(context)
    }
}