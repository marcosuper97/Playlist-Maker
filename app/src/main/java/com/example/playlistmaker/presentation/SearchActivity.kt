package com.example.playlistmaker.presentation

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.data.PreferencesManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TracksOnClickListener
import com.example.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {
    private val checkNetwork = Creator.getCheckNetwork()
    private val interactor = Creator.provideTrackInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private var searchQuery: String = STR_DEF
    private val searchHistory = PreferencesManager.getSearchHistory()
    private var tracks = mutableListOf<Track>()
    lateinit var tracksOnClickListener: TracksOnClickListener
    private val clearSearchHistory = Creator.clearSearchHistory()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding

    override fun onDestroy() {
        super.onDestroy()
        interactor.shutDown()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tracksOnClickListener = Creator.clickOnListenner(this)
        searchAdapter = SearchAdapter(tracksOnClickListener)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchAdapter

        chooseData()

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, STR_DEF) ?: STR_DEF
            binding.searchHint.setText(searchQuery)

            val tracksJson = savedInstanceState.getString(KEY_TRACKS_MEETING, "") ?: ""
            if (tracksJson.isNotEmpty()) {
                tracks = GsonClient.arrayFromJson(tracksJson)
                searchAdapter.updateData(tracks)
            }
        }
        binding.clearSearchHistory.setOnClickListener() {
            clearSearchHistory.perform()
            showSearchResult()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    binding.clearButton.visibility = clearButtonVisibility(s)
                    searchQuery = s.toString()
                    searchDebounce()
                }
            }

        }
        binding.searchHint.addTextChangedListener(simpleTextWatcher)
        binding.searchHint.requestFocus()
        binding.searchUpdate.setOnClickListener {
            searchDebounce()
        }
        binding.searchBack.setNavigationOnClickListener {
            finish()
            interactor.shutDown()
        }
        binding.clearButton.setOnClickListener {
            binding.progressBar.visibility = View.GONE
            binding.searchHint.setText(STR_DEF)
            chooseData()
            binding.clearButton.visibility = View.GONE
            hideKeyboard(this, binding.clearButton)
        }
    }

    private fun networkError() {
        binding.youSearchedIt.visibility = View.GONE
        binding.clearSearchHistory.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.searchUpdate.visibility = View.VISIBLE
        binding.errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.network_error))
        binding.errorStatus.setText(R.string.network_error)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private val searchRunnable = Runnable {
        when {
            searchQuery.isNotEmpty() && checkNetwork.isInternetAvailable(this) -> {
                searchRequest(searchQuery)
                showSearchResult()
            }

            searchQuery.isNotEmpty() && !checkNetwork.isInternetAvailable(this) -> {
                networkError()
            }

            else -> {
                chooseData()
            }
        }
    }

    private fun chooseData() {
        if (searchHistory.isEmpty() || binding.searchHint.text.isNotEmpty()) {
            showSearchResult()
        } else if (searchHistory.isNotEmpty()) {
            showSearchHistory()
        }
    }

    private fun showSearchHistory() {
        searchAdapter.tracks = searchHistory
        searchAdapter.notifyDataSetChanged()
        binding.youSearchedIt.visibility = View.VISIBLE
        binding.clearSearchHistory.visibility = View.VISIBLE
        binding.searchPlaceholder.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showSearchResult() {
        tracks.clear()
        searchAdapter.tracks = tracks
        binding.recyclerView.visibility = View.GONE
        binding.youSearchedIt.visibility = View.GONE
        binding.clearSearchHistory.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
    }

    private fun searchRequest(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.youSearchedIt.visibility = View.GONE
        binding.clearSearchHistory.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
        interactor.searchTracks(query, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>) {
                runOnUiThread {
                    if (foundTracks.isNotEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        searchAdapter.updateData(foundTracks)
                        binding.recyclerView.visibility = View.VISIBLE
                    } else if (foundTracks.isEmpty()) {
                        tracksNotFound()
                    }
                }
            }
        })
    }

    private fun tracksNotFound() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
        binding.searchUpdate.visibility = View.GONE
        binding.errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.tracks_not_found))
        binding.errorStatus.setText(R.string.tracks_not_found)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, searchQuery)
        val tracksJson = GsonClient.listToJson(tracks)
        outState.putString(KEY_TRACKS_MEETING, tracksJson)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, STR_DEF) ?: STR_DEF
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val KEY_SEARCH_QUERY: String = "SEARCH_QUERY"
        private const val STR_DEF: String = ""
        private const val KEY_TRACKS_MEETING = "tracks_meeting"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}