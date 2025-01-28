package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var searchQuery: String = STR_DEF
    private val iTunesService = ItunesApiClient.tunesService
    private var tracks = mutableListOf<Track>()
    private var isClickAllowed = true
    lateinit var searchHistory: MutableList<Track>
    lateinit var searchAdapter: SearchAdapter
    lateinit var searchError: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playerIntent = Intent(this, PlayerActivity::class.java)
        val searchInput = findViewById<EditText>(R.id.search_hint)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val searchBack = findViewById<Toolbar>(R.id.search_back)
        searchError = findViewById<LinearLayout>(R.id.searchPlaceholder)
        val errorImagePlaceholder = findViewById<ImageView>(R.id.errorImagePlaceholder)
        val errorStatus = findViewById<TextView>(R.id.errorStatus)
        val searchUpdate = findViewById<TextView>(R.id.searchUpdate)
        val recViewSearch = findViewById<RecyclerView>(R.id.recyclerView)
        val youSearchedIt = findViewById<TextView>(R.id.you_searched_it)
        val clearHistory = findViewById<TextView>(R.id.clear_search_history)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)


        searchHistory = PreferencesManager.getSearchHistory()
        searchAdapter = SearchAdapter(object : TracksOnClickListener {
            override fun onItemClick(track: Track) {
                if (clickDebounce()) {
                    try {
                        if (searchHistory.count() < MAX_COUNT_SEARCH_HISTORY && !searchHistory.contains(
                                track
                            )
                        ) {
                            searchHistory.add(0, track)
                            PreferencesManager.saveSearchHistory(searchHistory)
                            val putTrack = GsonClient.objectToJson(track)
                            playerIntent.putExtra("track", putTrack)
                            startActivity(playerIntent)
                        } else if (searchHistory.count() <= MAX_COUNT_SEARCH_HISTORY && searchHistory.contains(
                                track
                            )
                        ) {
                            searchHistory.remove(track)
                            searchHistory.add(0, track)
                            PreferencesManager.saveSearchHistory(searchHistory)
                            val putTrack = GsonClient.objectToJson(track)
                            playerIntent.putExtra("track", putTrack)
                            startActivity(playerIntent)
                        } else if (searchHistory.count() == MAX_COUNT_SEARCH_HISTORY && !searchHistory.contains(
                                track
                            )
                        ) {
                            searchHistory.removeAt(9)
                            searchHistory.add(0, track)
                            PreferencesManager.saveSearchHistory(searchHistory)
                            val putTrack = GsonClient.objectToJson(track)
                            playerIntent.putExtra("track", putTrack)
                            startActivity(playerIntent)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@SearchActivity,
                            "А вот тут я должен крашнуться, так как плеера то нет",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else Toast.makeText(this@SearchActivity,"АШЕЛАЕТЬ",LENGTH_LONG).show()
            }
        })

        recViewSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recViewSearch.adapter = searchAdapter

        fun showSearchHistory() {
            searchAdapter.tracks = searchHistory
            searchAdapter.notifyDataSetChanged()
            youSearchedIt.visibility = View.VISIBLE
            clearHistory.visibility = View.VISIBLE
            searchError.visibility = View.GONE
            recViewSearch.visibility = View.VISIBLE
        }

        fun showSearchResult() {
            tracks.clear()
            searchAdapter.tracks = tracks
            recViewSearch.visibility = View.GONE
            youSearchedIt.visibility = View.GONE
            clearHistory.visibility = View.GONE
            searchError.visibility = View.GONE
        }

        fun chooseData() {
            if (searchHistory.isEmpty() || searchInput.text.isNotEmpty()) {
                showSearchResult()
            } else if (searchHistory.isNotEmpty()) {
                showSearchHistory()
            }
        }
        chooseData()


        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, STR_DEF) ?: STR_DEF
            searchInput.setText(searchQuery)

            val tracksJson = savedInstanceState.getString(KEY_TRACKS_MEETING, "") ?: ""
            if (tracksJson.isNotEmpty()) {
                tracks = GsonClient.arrayFromJson(tracksJson)
                searchAdapter.updateData(tracks)
            }
        }

        clearHistory.setOnClickListener() {
            searchHistory.clear()
            PreferencesManager.clearSearchHistory()
            showSearchResult()
        }

        fun networkError() {
            recViewSearch.visibility = View.GONE
            searchError.visibility = View.VISIBLE
            searchUpdate.visibility = View.VISIBLE
            errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.network_error))
            errorStatus.setText(R.string.network_error)
        }

        fun tracksNotFound() {
            recViewSearch.visibility = View.GONE
            searchError.visibility = View.VISIBLE
            searchUpdate.visibility = View.GONE
            errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.tracks_not_found))
            errorStatus.setText(R.string.tracks_not_found)
        }

        fun searchQuestion(query: String) {
            progressBar.visibility = View.VISIBLE
            youSearchedIt.visibility = View.GONE
            clearHistory.visibility = View.GONE
            searchError.visibility = View.GONE
            iTunesService.search(query).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        tracks.clear()
                        recViewSearch.visibility = View.VISIBLE
                        searchError.visibility = View.GONE
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            searchAdapter.updateData(tracks)
                        }
                        if (tracks.isEmpty()) {
                            tracksNotFound()
                        }
                    } else {
                        networkError()
                    }
                }

                override fun onFailure(p0: Call<TrackResponse>, response: Throwable) {
                    progressBar.visibility = View.GONE
                    networkError()
                }
            })
        }

        val searchRunnable = Runnable {
            if (searchInput.text.toString().isNotEmpty()) {
                showSearchResult()
                searchQuestion(searchInput.text.toString())
            }else{
                chooseData()
            }
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                        clearButton.visibility = clearButtonVisibility(s)
                        searchQuery = s.toString()
                        searchDebounce()
                }
            }

        }


        searchInput.addTextChangedListener(simpleTextWatcher)

        searchInput.requestFocus()

        searchUpdate.setOnClickListener {
            showSearchResult()
            searchQuestion(searchInput.text.toString())
        }

        searchBack.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            progressBar.visibility = View.GONE
            searchInput.setText(STR_DEF)
            chooseData()
            clearButton.visibility = View.GONE
            hideKeyboard(this, clearButton)
        }
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

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val KEY_SEARCH_QUERY: String = "SEARCH_QUERY"
        private const val STR_DEF: String = ""
        private const val KEY_TRACKS_MEETING = "tracks_meeting"
        private const val MAX_COUNT_SEARCH_HISTORY = 10
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}