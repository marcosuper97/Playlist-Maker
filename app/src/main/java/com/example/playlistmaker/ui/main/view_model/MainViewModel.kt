package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationInteractor
import com.example.playlistmaker.domain.main_menu_navigation.Navigation
import com.example.playlistmaker.util.SingleOnClickEvent

class MainViewModel(
    val navigationInteractor: MainNavigationInteractor,
):ViewModel() {

    private val _menuEvent = SingleOnClickEvent<Navigation>()
    val menuEvent: LiveData<Navigation> get() = _menuEvent

    fun openSearch(){
        navigationInteractor.search()
    }

    fun openLibrary(){
        navigationInteractor.library()
    }

    fun openSettings(){
        navigationInteractor.settings()
    }

    fun clickOnSearch(){
        _menuEvent.postValue(Navigation.Search)
    }

    fun clickOnLibrary(){
        _menuEvent.postValue(Navigation.Library)
    }

    fun clickOnSettings(){
        _menuEvent.postValue(Navigation.Settings)
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