package com.example.playlistmaker.data.sharing

import android.content.Context

interface SharingRepository {
    fun shareApp(context: Context)
    fun openTerms(context: Context)
    fun openSupport(context: Context)
}