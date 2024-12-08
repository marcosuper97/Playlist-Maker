package com.example.playlistmaker
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {
    private var searchQuery: String = STR_DEF
    private val iTunesService = ItunesApiClient.tunesService
    private var tracks = ArrayList<Track>()
    private val adapter = TrackSearchAdapter()
    lateinit var searchError: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchTwo)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        adapter.tracks = tracks
        val recViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch)
            recViewSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recViewSearch.adapter=adapter
        val searchInput = findViewById<EditText>(R.id.search_hint)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val searchBack = findViewById<Button>(R.id.search_back)
        searchError = findViewById<LinearLayout>(R.id.searchPlaceholder)
        val errorImagePlaceholder = findViewById<ImageView>(R.id.errorImagePlaceholder)
        val errorStatus = findViewById<TextView>(R.id.errorStatus)
        val searchUpdate = findViewById<TextView>(R.id.searchUpdate)
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (tracks.isNotEmpty()){
                    clearButton.visibility = View.VISIBLE
                }
                searchQuery = s.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        }

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, STR_DEF) ?: STR_DEF
            searchInput.setText(searchQuery)

            val tracksJson = savedInstanceState.getString(KEY_TRACKS_MEETING, "") ?: ""
            if(tracksJson.isNotEmpty()) {
                tracks = fromJson(tracksJson)
                adapter.updateData(tracks)
            }
        }

        fun networkError(){
            recViewSearch.visibility= View.GONE
            searchUpdate.visibility=View.VISIBLE
            searchError.visibility = View.VISIBLE
            errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.network_error))
            errorStatus.setText(R.string.network_error)
        }

        fun tracksNotFound(){
            recViewSearch.visibility= View.GONE
            searchUpdate.visibility=View.GONE
            searchError.visibility = View.VISIBLE
            errorImagePlaceholder.setImageDrawable(getDrawable(R.drawable.tracks_not_found))
            errorStatus.setText(R.string.tracks_not_found)
        }

        fun searchQuestion(query: String){
            iTunesService.search(query).enqueue(object: Callback<TrackResponse>{
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>){
                    if (response.isSuccessful) {
                        tracks.clear()
                        recViewSearch.visibility=View.VISIBLE
                        searchError.visibility=View.GONE
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            adapter.updateData(tracks)
                        }
                        if (tracks.isEmpty()){
                            tracksNotFound()
                        }
                    }else{
                        networkError()
                    }
                }
                override fun onFailure(p0: Call<TrackResponse>, response: Throwable){
                    networkError()
                }
            })
        }

        searchInput.addTextChangedListener(simpleTextWatcher)
        searchInput.requestFocus()
        searchInput.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(searchInput.text.toString().isNotEmpty()) {
                    searchQuestion(searchInput.text.toString())
                }else {
                    Toast.makeText(this, getString(R.string.emptyText), Toast.LENGTH_SHORT).show()
                }
            }
            false
        }

        searchUpdate.setOnClickListener{
            searchQuestion(searchInput.text.toString())
            searchError.visibility=View.GONE
        }

        searchBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchInput.setText(STR_DEF)
            tracks.clear()
            adapter.notifyDataSetChanged()
            searchError.visibility=View.GONE
            clearButton.visibility = View.GONE
            hideKeyboard(this, clearButton)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY,searchQuery)
        val tracksJson = toJson(tracks)
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
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun toJson(tracks: ArrayList<Track>): String {
        val gson = Gson()
        return gson.toJson(tracks)
    }

    private fun fromJson(json: String): ArrayList<Track> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    companion object {
        const val KEY_SEARCH_QUERY: String = "SEARCH_QUERY"
        const val STR_DEF: String = ""
        const val KEY_TRACKS_MEETING = "tracks_meeting"
    }
}

