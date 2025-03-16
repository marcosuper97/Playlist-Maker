package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.network.NetworkCheckingInteractor
import com.example.playlistmaker.domain.search.State
import com.example.playlistmaker.domain.search.StorageGetSetInterractor
import com.example.playlistmaker.domain.search.StoreCleanerInterractor
import com.example.playlistmaker.domain.search.SearchTrackInteractor

class SearchViewModel(
    private val storeGetSetInterractor: StorageGetSetInterractor,
    private var cleanSearchHistory: StoreCleanerInterractor?,
    private var checking: NetworkCheckingInteractor?,
    private var trackSearchInteractor: SearchTrackInteractor?,
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
        storeGetSetInterractor?.getPreferences()?.let { cleanSearchHistory?.execute() }
        _screenState.postValue(State.EmptyScreen)
    }

    fun toSearchRequest(query: String){
        if(checking?.isInternetAvailable() == true){
            _screenState.postValue(State.Loading)
            trackSearchInteractor?.searchTracks(query, object : SearchTrackInteractor.TrackConsumer {
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
}
