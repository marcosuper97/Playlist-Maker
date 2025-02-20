package com.example.playlistmaker.data

import android.content.Context

interface NetworkChecking {
    fun isInternetAvailable(context: Context): Boolean
}