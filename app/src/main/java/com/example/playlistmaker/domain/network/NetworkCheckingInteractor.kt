package com.example.playlistmaker.domain.network

interface NetworkCheckingInteractor {
    fun isInternetAvailable(): Boolean
}