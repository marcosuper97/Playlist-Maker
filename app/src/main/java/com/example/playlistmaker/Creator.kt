package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.ClearTrackHistoryImpl
import com.example.playlistmaker.data.TrackHistoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.ClearData
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksOnClickListener
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.impl.TrackOnClickListenerImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun clickOnListenner(context: Context): TracksOnClickListener {
        return TrackOnClickListenerImpl(context, TrackHistoryImpl())
    }

    fun clearSearchHistory(): ClearData{
        return ClearTrackHistoryImpl()
    }
}