package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.settings.SettingsEvent
import com.example.playlistmaker.domain.settings.ThemeChangerInteractor
import com.example.playlistmaker.domain.shairing.SharingInteractor
import com.example.playlistmaker.util.SingleOnClickEvent

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeChangerInteractor: ThemeChangerInteractor
) : ViewModel() {

    private val _settingsEvent = SingleOnClickEvent<SettingsEvent>()
    val settingsEvent: LiveData<SettingsEvent> get() = _settingsEvent

    init {
        _settingsEvent.postValue(SettingsEvent.SwapTheme(themeChangerInteractor.getThemeStatus()))
    }

    fun supportEvent() {
        sharingInteractor.openSupport()
    }

    fun termsEvent() {
        sharingInteractor.openTerms()
    }

    fun shareAppEvent() {
        sharingInteractor.shareApp()
    }

    fun changeThemeEvent(check: Boolean) {
        themeChangerInteractor.changeTheme(check)
    }

    fun clickOnSupport(){
        _settingsEvent.postValue(SettingsEvent.OpenSupport)
    }

    fun clickOnTerms(){
        _settingsEvent.postValue(SettingsEvent.OpenTerms)
    }

    fun clickOnShareApp(){
        _settingsEvent.postValue(SettingsEvent.ShareApp)
    }

    fun clickOnChangeTheme(check:Boolean){
        _settingsEvent.postValue(SettingsEvent.SwapTheme(check))
    }
}