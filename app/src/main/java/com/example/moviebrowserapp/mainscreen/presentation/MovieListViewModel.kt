package com.example.moviebrowserapp.mainscreen.presentation

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.domain.movielistlogic.MovieListUseCase
import com.example.moviebrowserapp.mainscreen.domain.movielistlogic.MoviePaginationImpl
import com.example.moviebrowserapp.mainscreen.domain.searchquerylogic.SearchQueryPaginationImpl
import com.example.moviebrowserapp.mainscreen.domain.searchquerylogic.SearchQueryRepo
import com.example.moviebrowserapp.mainscreen.domain.searchquerylogic.SearchQueryUseCase
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryUi
import com.example.moviebrowserapp.network.CheckNetworkConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    val movieListUseCase: MovieListUseCase,
    val searchQueryUseCase: SearchQueryUseCase,
    val checkNetworkConnection: CheckNetworkConnection,
    val movieDao: MovieFirstListDao,
) : ViewModel() {

    private var _state = MutableStateFlow(MainScreenState())
    var state: StateFlow<MainScreenState> = _state.asStateFlow()
    private var currentSearchQuery: String? = null


init {
    events(MainScreenIntent.CheckNetworkConnection)
}


    lateinit var pageSource: MoviePaginationImpl

    val moviePage = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { moviePageSource() }
    ).flow.cachedIn(viewModelScope)


private val searchQueryFlow = _state.map { it.searchQuery }.distinctUntilChanged()
val searchPage = searchQueryFlow
    .flatMapLatest { query ->
        if (query.isBlank()) {
            setSearchToFalse()
            flowOf(PagingData.empty())
        } else {
            Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    SearchQueryPaginationImpl(
                        searchQueryUseCase,
                        query,
                        checkNetworkConnection
                    )
                }
            ).flow
        }
    }
    .cachedIn(viewModelScope)

    // Add a function to check if we already have search results
//    fun hasSearchResults(): Boolean {
//        return _searchPage != null && currentSearchQuery != null
//    }
//
//    // Add a function to get the current search query without triggering recreation
//    fun getCurrentSearchQuery(): String? {
//        return currentSearchQuery
//    }
//
//    // Add a function to check if search results are already loaded
//    fun isSearchResultsLoaded(): Boolean {
//        return _searchPage != null && currentSearchQuery != null &&
//               _state.value.searchQuery == currentSearchQuery
//    }

    // Add a function to preserve search results across configuration changes
    fun preserveSearchResults() {
        // This function can be called to ensure search results are preserved
        // during configuration changes like rotation
        if (_state.value.searchQuery.isNotBlank() && currentSearchQuery != null) {
            Log.d("search", "Preserving search results for: ${_state.value.searchQuery}")
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun setRefreshing(value: Boolean) {
        _isRefreshing.value = value
    }

    fun moviePageSource(): MoviePaginationImpl {
        pageSource = MoviePaginationImpl(movieListUseCase, checkNetworkConnection)
        return pageSource
    }



    fun events(event: MainScreenIntent) {
        when (event) {
            is MainScreenIntent.LoadMovieOffline ->{}
            is MainScreenIntent.SearchQuery ->{ //searchQuery(event.query)
                _state.value = _state.value.copy(searchQuery = event.query, isSearching = true)
                 }
            is MainScreenIntent.RefreshScreen -> {
                refreshScreen()
            }
            is MainScreenIntent.CheckNetworkConnection ->{
                val checkIfOnline = checkNetworkConnection.isOnline()
                _state.value = _state.value.copy(isOffline = !checkIfOnline)            }
            is MainScreenIntent.Retry -> retry()
        }
    }




    fun getMoviesOffline (){
        viewModelScope.launch {
          val movieList = movieDao.getAllMovies()
            val uiList = movieListUseCase.run { movieList.toUiList() }

            _state.value = _state.value.copy(movieListOffline = uiList)

        }
    }


    fun refreshScreen() {
        _isRefreshing.value = true

        val checkIfOnline = checkNetworkConnection.isOnline()
        if (!checkIfOnline) {
            _state.value = _state.value.copy(isOffline = true)
            getMoviesOffline()
            // Keep spinner for a short moment to show feedback, then hide
            viewModelScope.launch {
                kotlinx.coroutines.delay(600)
                _isRefreshing.value = false
            }
        } else {
            _state.value = _state.value.copy(isOffline = false)
            pageSource.invalidate()
            // Spinner will be turned off from UI when paging reports NotLoading
        }
    }

    fun restoreRefreashValue() {
        _state.value = _state.value.copy(isRefreshingScreen = false)
    }

    fun setSearchToFalse() {
        _state.value = _state.value.copy(isSearching = false)
    }


    fun retry() {
        // Invalidate the current paging source to retry loading
        if (::pageSource.isInitialized) {
            pageSource.invalidate()
        }
        // Also check network connection
        events(MainScreenIntent.CheckNetworkConnection)
    }


}