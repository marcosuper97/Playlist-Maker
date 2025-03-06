package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.data.network.NetworkChecking
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.State
import com.example.playlistmaker.domain.search.StorageGetSetInterractor
import com.example.playlistmaker.domain.search.StoreCleanerInterractor
import com.example.playlistmaker.domain.search.TrackInteractor

class SearchViewModel(
    private val storeGetSetInterractor: StorageGetSetInterractor,
    private var cleanSearchHistory: StoreCleanerInterractor?,
    private var checking: NetworkChecking?,
    private var trackSearchInteractor: TrackInteractor?,
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        cleanSearchHistory = null
        checking = null
        trackSearchInteractor = null
    }

    private val _screenState = MutableLiveData<State>(State.Loading)
    val screenState: LiveData<State> get() = _screenState

    init {
        val trackSet = storeGetSetInterractor.getSearchHistory()
        if (trackSet.isEmpty()){
            _screenState.postValue(State.EmptyScreen)
        }else _screenState.postValue(State.ShowHistoryContent(trackSet))
    }

     fun clickOnClearButton(){
        val trackHistory = storeGetSetInterractor?.getSearchHistory()
         if (trackHistory?.isNotEmpty() == true)
         _screenState.postValue(State.ShowHistoryContent(trackHistory))
         else _screenState.postValue(State.EmptyScreen)
     }

    fun onTrackClicked(track:Track){
        storeGetSetInterractor.saveTrackInHistory(track)
    }

    fun onClickSearchClear(){
        storeGetSetInterractor?.getPreferences()?.let { cleanSearchHistory?.execute(it) }
        _screenState.postValue(State.EmptyScreen)
    }

    fun toSearchRequest(query: String){
        if(checking?.isInternetAvailable() == true){
            _screenState.postValue(State.Loading)
            trackSearchInteractor?.searchTracks(query, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>) {
                    if (foundTracks.isNotEmpty()) {
                        _screenState.postValue(State.ShowSearchContent(foundTracks))
                    } else {
                        _screenState.postValue(State.NothingFound)
                    }
                }
            }
            )
        }else _screenState.postValue(State.NetworkError)
    }

    companion object {
        fun getViewModelFactory(
            storeGetSetInterractor: StorageGetSetInterractor,
            cleanSearchHistory: StoreCleanerInterractor?,
            checking: NetworkChecking?,
            trackSearchInteractor: TrackInteractor?,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    storeGetSetInterractor,
                    cleanSearchHistory,
                    checking,
                    trackSearchInteractor,
                )
            }
        }
    }
}
