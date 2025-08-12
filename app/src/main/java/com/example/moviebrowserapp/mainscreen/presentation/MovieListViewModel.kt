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

//    init {
//        if(!checkNetworkConnection.isOnline() && _state.value.movieList.isNotEmpty()){
//            getMovieList()
//        }
//    }

    lateinit var pageSource: MoviePaginationImpl

    val moviePage = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { moviePageSource() }
    ).flow.cachedIn(viewModelScope)

    var _searchPage:Flow<PagingData<SearchQueryUi>>? = null
    val searchPage: Flow<PagingData<SearchQueryUi>>?
        get() = _searchPage

    fun moviePageSource(): MoviePaginationImpl {
        pageSource = MoviePaginationImpl(movieListUseCase, checkNetworkConnection,movieDao)
        return pageSource
    }


    fun events(event: MainScreenIntent) {
        when (event) {
            is MainScreenIntent.LoadMovie -> moviePage
            is MainScreenIntent.SearchQuery -> searchQuery(event.query)
            is MainScreenIntent.RefreshScreen -> refreshScreen()
            is MainScreenIntent.Retry -> retry()
        }
    }

    fun getMovieList() {

    }



    fun searchQuery(query: String) {
        Log.d("query ana", "ana hena $query")
        if(_state.value.searchQuery ==currentSearchQuery && searchPage != null ){
            Log.d("yes equal" , "equal a7a")
            return
        }
            currentSearchQuery=query
           _searchPage=  Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    SearchQueryPaginationImpl(
                        searchQueryUseCase,
                        query,
                        checkNetworkConnection
                    )
                }
            ).flow
            _state.value = _state.value.copy(searchQuery = query)

    }


    fun clearSearch() {
        _state.update { currentState ->
            currentState.copy(
                isSearching = false,
                searchQuery = ""
            )
        }

    }

    fun restoreSearchState() {

    }


    fun refreshScreen() {
        viewModelScope.launch {

            pageSource.invalidate()
            _state.value = _state.value.copy(isRefreshingScreen = true)
        }
    }

    fun restoreRefreashValue() {
        _state.value = _state.value.copy(isRefreshingScreen = false)
    }

    fun setSearchToFalse() {
        _state.value = _state.value.copy(isSearching = false)
    }


    fun retry() {

    }
}