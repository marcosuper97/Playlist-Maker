package com.example.playlistmaker.domain.main_menu_navigation.impl

import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationInteractor
import com.example.playlistmaker.domain.main_menu_navigation.MainNavigationRepository

class MainNavigationInteractorImpl(private val mainNavigationRepository: MainNavigationRepository) : MainNavigationInteractor {
    override fun library() {
        mainNavigationRepository.library()
    }

    override fun search() {
        mainNavigationRepository.search()
    }

    override fun settings() {
        mainNavigationRepository.settings()
    }
}