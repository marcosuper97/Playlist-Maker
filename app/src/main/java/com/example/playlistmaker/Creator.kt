package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.ClearTrackHistoryImpl
import com.example.playlistmaker.data.TrackHistoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.ClearTrackHistory
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksOnClickListener
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.data.TrackOnClickListenerImpl
import com.example.playlistmaker.domain.api.AppNavigation
import com.example.playlistmaker.domain.api.FeedbackLink
import com.example.playlistmaker.domain.api.UserMediaPlayer
import com.example.playlistmaker.domain.impl.FeedBackMail
import com.example.playlistmaker.domain.impl.MainNavigation
import com.example.playlistmaker.domain.impl.ShareApp
import com.example.playlistmaker.domain.impl.UserAgreement
import com.example.playlistmaker.domain.impl.UserMediaPlayerImpl

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

    fun clearSearchHistory(): ClearTrackHistory{
        return ClearTrackHistoryImpl()
    }

    fun getSendFeedBack() : FeedbackLink{
        return FeedBackMail()
    }

    fun getUserAgreement() : FeedbackLink{
        return UserAgreement()
    }

    fun getShareLink(): FeedbackLink{
        return ShareApp()
    }

    fun getAppNavigation(): AppNavigation{
        return MainNavigation()
    }

    fun mediaPlayer(): UserMediaPlayer{
        return UserMediaPlayerImpl()
    }
}