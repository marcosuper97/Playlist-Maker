package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.MediaPlayerTrack
import com.example.playlistmaker.data.NetworkChecking
import com.example.playlistmaker.data.StoreCleanerRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksOnClickListener
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.data.TrackOnClickListenerImpl
import com.example.playlistmaker.data.StoreGetSetRepositoryImpl
import com.example.playlistmaker.data.TrackHistoryReformaterImpl
import com.example.playlistmaker.domain.api.AppNavigation
import com.example.playlistmaker.domain.api.FeedbackLink
import com.example.playlistmaker.domain.api.UserMediaPlayer
import com.example.playlistmaker.domain.api.GetPlayerTrack
import com.example.playlistmaker.domain.impl.FeedBackMail
import com.example.playlistmaker.domain.impl.MainNavigation
import com.example.playlistmaker.domain.impl.ShareApp
import com.example.playlistmaker.domain.impl.UserAgreement
import com.example.playlistmaker.data.UserMediaPlayerImpl
import com.example.playlistmaker.data.dto.CoverForPlayerImpl
import com.example.playlistmaker.data.network.CoverForPlayer
import com.example.playlistmaker.data.network.NetworkAvailable
import com.example.playlistmaker.domain.api.ThemeChanger
import com.example.playlistmaker.domain.api.StorageGetSetInterractor
import com.example.playlistmaker.domain.api.StoreCleanerInterractor
import com.example.playlistmaker.domain.api.StoreCleanerRepository
import com.example.playlistmaker.domain.api.StoreGetSetRepository
import com.example.playlistmaker.domain.api.TrackHistoryReformater
import com.example.playlistmaker.domain.impl.PlayerInterractorImpl
import com.example.playlistmaker.domain.impl.StorageCleanerInterractorImpl
import com.example.playlistmaker.domain.impl.SwapChangerImpl
import com.example.playlistmaker.domain.impl.StorageGetSetInterractorImpl

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }


    fun getSendFeedBack(): FeedbackLink {
        return FeedBackMail()
    }

    fun getUserAgreement(): FeedbackLink {
        return UserAgreement()
    }

    fun getShareLink(): FeedbackLink {
        return ShareApp()
    }

    fun getAppNavigation(): AppNavigation {
        return MainNavigation()
    }


    fun getPlayerTrack(): GetPlayerTrack {
        return MediaPlayerTrack()
    }

    fun mediaPlayer(): UserMediaPlayer {
        return UserMediaPlayerImpl()
    }

    fun getPlayerInterractor():PlayerInterractorImpl{
        return PlayerInterractorImpl(mediaPlayer())
    }

    fun trackCover(): CoverForPlayer {
        return CoverForPlayerImpl()
    }

    fun getCheckNetwork(): NetworkChecking {
        return NetworkAvailable()
    }

    fun getThemeChanger(): ThemeChanger {
        return SwapChangerImpl()
    }

    fun getStoreGetSetRepository(context: Context): StoreGetSetRepository {
        return StoreGetSetRepositoryImpl(context)
    }

    fun getTrackGetSetInterractor(context: Context): StorageGetSetInterractor {
        return StorageGetSetInterractorImpl(getStoreGetSetRepository(context))
    }

    fun getTrackHistoryReformator(context: Context): TrackHistoryReformater {
        return TrackHistoryReformaterImpl(getTrackGetSetInterractor(context))
    }

    fun clickOnListenner(context: Context): TracksOnClickListener {
        return TrackOnClickListenerImpl(context, getTrackHistoryReformator(context))
    }

    fun getCleanStoreRepository(sharedPreferences: SharedPreferences): StoreCleanerRepository {
        return StoreCleanerRepositoryImpl(sharedPreferences)
    }

    fun getCleanStoreInterractor(sharedPreferences: SharedPreferences): StoreCleanerInterractor {
        return StorageCleanerInterractorImpl(getCleanStoreRepository(sharedPreferences))
    }
}
