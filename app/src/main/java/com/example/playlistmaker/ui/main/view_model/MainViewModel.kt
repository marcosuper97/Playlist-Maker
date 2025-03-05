package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationInteractor

class MainViewModel(
    val navigationInteractor: MainNavigationInteractor,
):ViewModel() {

    fun onCLickSearch(){
        navigationInteractor.search()
    }

    fun onCLickLibrary(){
        navigationInteractor.library()
    }

    fun onCLickSettings(){
        navigationInteractor.settings()
    }


    companion object {
        fun getViewModelFactory(mainNavigationInteractor: MainNavigationInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    mainNavigationInteractor
                )
            }
        }
    }
}