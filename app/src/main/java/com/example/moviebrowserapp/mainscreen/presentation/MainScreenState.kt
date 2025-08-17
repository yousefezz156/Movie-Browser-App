package com.example.moviebrowserapp.mainscreen.presentation

import android.text.BoringLayout
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListUi
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryUi

data class MainScreenState(


    val isSearching : Boolean = false,
    val searchQuery : String = "",
    val isLoading : Boolean = false,
    val isRefreshingScreen: Boolean = false,
    val error : String? = null,
    val movieListOffline : List<MovieListUi> = emptyList(),
    val isOffline : Boolean = false,
)
