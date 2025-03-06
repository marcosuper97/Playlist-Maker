package com.example.playlistmaker.data.network

import android.content.Context

interface NetworkChecking {
    fun isInternetAvailable(): Boolean
}