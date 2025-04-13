package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.ui.library.view_model_fragments.FavoriteTracksViewModel
import com.example.playlistmaker.ui.library.view_model_fragments.LibraryFragmentViewModel
import com.example.playlistmaker.ui.library.view_model_fragments.MediaLibraryViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModel = module {

    viewModel { (trackToJson: String) ->
        PlayerViewModel(trackToJson, get(), get())
    }

    viewModel{(context: Context) ->
        SearchViewModel(get(),get(),get{ parametersOf(context) },get())
    }

    viewModel{ (context: Context) ->
        SettingsViewModel(get { parametersOf(context) },get{ parametersOf(context) })
    }

    viewModel{
        FavoriteTracksViewModel()
    }

    viewModel{
        LibraryFragmentViewModel()
    }

    viewModel{
        MediaLibraryViewModel()
    }


}