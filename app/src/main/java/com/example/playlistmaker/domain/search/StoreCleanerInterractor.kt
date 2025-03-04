package com.example.playlistmaker.domain.search

import android.content.SharedPreferences

interface StoreCleanerInterractor {
    fun execute(sharedPreferences: SharedPreferences)
}