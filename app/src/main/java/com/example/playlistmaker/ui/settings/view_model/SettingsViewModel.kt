package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.settings.SettingsEvent
import com.example.playlistmaker.domain.settings.ThemeChangerInteractor
import com.example.playlistmaker.domain.shairing.SharingInteractor
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.SingleOnClickEvent

class SettingsViewModel(
    val sharingInteractor: SharingInteractor,
    val themeChangerInteractor: ThemeChangerInteractor
) : ViewModel() {


    private val _settingsEvent = SingleOnClickEvent<SettingsEvent>()
    val settingsEvent: LiveData<SettingsEvent> get() = _settingsEvent

    fun onSupportClicked() {
        _settingsEvent.value = SettingsEvent.OpenSupport
    }

    fun onTermsClicked() {
        _settingsEvent.value = SettingsEvent.OpenTerms
    }

    fun onShareAppClicked() {
        _settingsEvent.value = SettingsEvent.ShareApp
    }

    fun onChangeThemeClicked() {
        _settingsEvent.value = SettingsEvent.SwapTheme
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    sharingInteractor = Creator.getSharingInteractor(),
                    themeChangerInteractor = Creator.getThemeChangerInteractor()
                )
            }
        }
    }
}