package com.example.playlistmaker.data.network

interface NetworkCheckingRepository {
    fun isInternetAvailable(): Boolean
}