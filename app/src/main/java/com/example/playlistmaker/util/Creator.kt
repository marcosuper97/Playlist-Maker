package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.data.main_menu_navigation.MainNavigationRepositoryImpl
import com.example.playlistmaker.data.network.NetworkAvailable
import com.example.playlistmaker.data.network.NetworkChecking
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.StorageGetSetInterractorImpl
import com.example.playlistmaker.data.search.StoreCleanerRepositoryImpl
import com.example.playlistmaker.data.search.StoreGetSetRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.ThemeChangerRepository
import com.example.playlistmaker.data.settings.impl.ThemeChangerRepositoryImpl
import com.example.playlistmaker.data.sharing.SharingRepository
import com.example.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationInteractor
import com.example.playlistmaker.domain.main_menu_navigation.impl.MainNavigationInteractorImpl
import com.example.playlistmaker.domain.player.GetPlayerTrack
import com.example.playlistmaker.domain.player.UserMediaPlayer
import com.example.playlistmaker.domain.player.impl.GetPlayerTrackImpl
import com.example.playlistmaker.domain.player.impl.PlayerInterractorImpl
import com.example.playlistmaker.data.player.impl.UserMediaPlayerImpl
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationRepository
import com.example.playlistmaker.domain.search.SearchTrackRepository
import com.example.playlistmaker.domain.search.StorageGetSetInterractor
import com.example.playlistmaker.domain.search.StoreCleanerInterractor
import com.example.playlistmaker.domain.search.StoreCleanerRepository
import com.example.playlistmaker.domain.search.StoreGetSetRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.impl.StorageCleanerInterractorImpl
import com.example.playlistmaker.domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.ThemeChangerInteractor
import com.example.playlistmaker.domain.settings.impl.ThemeChangerInteractorImpl
import com.example.playlistmaker.domain.shairing.SharingInteractor
import com.example.playlistmaker.domain.shairing.impl.SharingInteractorImpl
import kotlin.contracts.contract

object Creator {

    private fun getSharingRepository(context: Context): SharingRepository{
        return SharingRepositoryImpl(context)
    }

    fun getSharingInteractor(context: Context): SharingInteractor{
        return SharingInteractorImpl(getSharingRepository(context))
    }

    fun getThemeChangerRepository(): ThemeChangerRepository {
        return ThemeChangerRepositoryImpl()
    }

    fun getThemeChangerInteractor(): ThemeChangerInteractor{
        return ThemeChangerInteractorImpl(getThemeChangerRepository())
    }

    private fun getTrackRepository(): SearchTrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun getMainNavigationRepository(context: Context): MainNavigationRepository {
        return MainNavigationRepositoryImpl(context)
    }

    fun getMainNavigationInteractor(context: Context): MainNavigationInteractor {
        return MainNavigationInteractorImpl(getMainNavigationRepository(context))
    }

    fun getPlayerTrack(): GetPlayerTrack {
        return GetPlayerTrackImpl()
    }

    fun mediaPlayer(): UserMediaPlayer {
        return UserMediaPlayerImpl()
    }

    fun getPlayerInterractor(): PlayerInterractorImpl {
        return PlayerInterractorImpl(mediaPlayer())
    }

    fun getCheckNetwork(context: Context): NetworkChecking {
        return NetworkAvailable(context)
    }

    fun getStoreGetSetRepository(context: Context): StoreGetSetRepository {
        return StoreGetSetRepositoryImpl(context)
    }

    fun getTrackGetSetInterractor(context: Context): StorageGetSetInterractor {
        return StorageGetSetInterractorImpl(getStoreGetSetRepository(context))
    }

    fun getCleanStoreRepository(): StoreCleanerRepository {
        return StoreCleanerRepositoryImpl()
    }

    fun getCleanStoreInterractor(): StoreCleanerInterractor {
        return StorageCleanerInterractorImpl(getCleanStoreRepository())
    }
}
