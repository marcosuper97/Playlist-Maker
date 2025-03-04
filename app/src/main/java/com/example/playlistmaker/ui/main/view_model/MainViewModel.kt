package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationInteractor
import com.example.playlistmaker.domain.main_menu_navigation.Navigation
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.SingleOnClickEvent

class MainViewModel(
    val navigationInteractor: MainNavigationInteractor,
):ViewModel() {

    private val _navigate = SingleOnClickEvent<Navigation>()
    val navigate: LiveData<Navigation> get() = _navigate

    fun onCLickSearch(){
        _navigate.value = Navigation.Search
    }

    fun onCLickLibrary(){
        _navigate.value = Navigation.Library
    }

    fun onCLickSettings(){
        _navigate.value = Navigation.Settings
    }


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    navigationInteractor = Creator.getMainNavigationInteractor()
                )
            }
        }
    }
}