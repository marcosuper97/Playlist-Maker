package com.example.playlistmaker.domain.network.impl

import com.example.playlistmaker.data.network.NetworkCheckingRepository
import com.example.playlistmaker.domain.network.NetworkCheckingInteractor

class NetworkCheckingInteractorImpl(private val networkCheckingRepository: NetworkCheckingRepository):NetworkCheckingRepository,
    NetworkCheckingInteractor {
    override fun isInternetAvailable(): Boolean {
        return networkCheckingRepository.isInternetAvailable()
    }
}