package com.example.playlistmaker.domain.shairing

import android.content.Context

interface SharingInteractor {
    fun shareApp(context: Context)
    fun openTerms(context: Context)
    fun openSupport(context: Context)
}