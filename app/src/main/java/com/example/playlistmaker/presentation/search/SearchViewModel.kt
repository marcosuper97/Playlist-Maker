package com.example.playlistmaker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.viewModel
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.SearchTrackInteractor
import com.example.playlistmaker.util.SearchState
import com.example.playlistmaker.domain.search.StorageGetSetInterractor
import com.example.playlistmaker.domain.search.StoreCleanerInterractor
import kotlinx.coroutines.launch

class SearchViewModel(
    private val storeGetSetInterractor: StorageGetSetInterractor,
    private var cleanSearchHistory: StoreCleanerInterractor?,
    private var trackSearchInteractor: SearchTrackInteractor?,
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        cleanSearchHistory = null
        trackSearchInteractor = null
    }

    private val _screenState = MutableLiveData<SearchState>(SearchState.Loading)
    val screenState: LiveData<SearchState> get() = _screenState

    init {
        val trackSet = storeGetSetInterractor.getSearchHistory()
        if (trackSet.isEmpty()) {
            _screenState.postValue(SearchState.EmptyScreen)
        } else _screenState.postValue(SearchState.ShowHistoryContent(trackSet))
    }

    fun clearSearchRequest() {
        val trackHistory = storeGetSetInterractor?.getSearchHistory()
        if (trackHistory?.isNotEmpty() == true)
            _screenState.postValue(SearchState.ShowHistoryContent(trackHistory))
        else _screenState.postValue(SearchState.EmptyScreen)
    }

    fun onTrackClicked(track: Track) {
        storeGetSetInterractor.saveTrackInHistory(track)
    }

    fun onClickSearchClear() {
        storeGetSetInterractor.getPreferences()?.let { cleanSearchHistory?.execute() }
        _screenState.postValue(SearchState.EmptyScreen)
    }

    fun toSearchRequest(query: String) {
        if (query.isNotEmpty()) {
            _screenState.postValue(SearchState.Loading)

            viewModelScope.launch {
                trackSearchInteractor?.searchTracks(query)?.collect { pair ->
                    processResult(pair.first,pair.second)
                }
            }
        }
    }

    fun processResult(foundTrack: List<Track>?, errorState: SearchState?) {
        when {
            errorState != null -> {
                _screenState.postValue(errorState!!)
            Log.d("stateERROR", "$errorState")// Сначала обрабатываем ошибку
            }
            foundTrack!!.isEmpty() -> {
                _screenState.postValue(SearchState.NothingFound)
                Log.d("stateNOTHING", "$errorState")
            }
            foundTrack.isNotEmpty() -> {
                _screenState.postValue(SearchState.ShowSearchContent(foundTrack))
                Log.d("stateFOUND", "$errorState")
            }
        }
    }
}
