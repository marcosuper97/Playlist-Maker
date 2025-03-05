package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.settings.ThemeChangerInteractor
import com.example.playlistmaker.domain.shairing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeChangerInteractor: ThemeChangerInteractor
) : ViewModel() {

    private val _changeTheme = MutableLiveData<Boolean>()
    val changeTheme: LiveData<Boolean> get() = _changeTheme

    init {
        _changeTheme.postValue(themeChangerInteractor.getThemeStatus())
    }

    fun onSupportClicked() {
        sharingInteractor.openSupport()
    }

    fun onTermsClicked() {
        sharingInteractor.openTerms()
    }

    fun onShareAppClicked() {
        sharingInteractor.shareApp()
    }

    fun onChangeThemeClicked(check: Boolean) {
        _changeTheme.postValue(check)
        themeChangerInteractor.changeTheme(check)
    }

    companion object {
        fun getViewModelFactory(
            sharingInteractor: SharingInteractor,
            themeChangerInteractor: ThemeChangerInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    sharingInteractor,
                    themeChangerInteractor,
                )
            }
        }
    }
}