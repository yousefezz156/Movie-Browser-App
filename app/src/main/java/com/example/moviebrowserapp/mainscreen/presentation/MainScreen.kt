package com.example.moviebrowserapp.mainscreen.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingSource.LoadResult
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.util.query
import com.example.moviebrowserapp.R
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryUi
import com.example.moviebrowserapp.mainscreen.presentation.SearchQueryCardView
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenScaffold(
    movieListViewModel: MovieListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.popular_movie)) })
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            MainScreen(movieListViewModel = movieListViewModel, navController = navController)
        }
    }
}

@Composable
fun MainScreen(
    movieListViewModel: MovieListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by movieListViewModel.state.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query=it
                //movieListViewModel.events(MainScreenIntent.SearchQuery(query = query))
                movieListViewModel.searchQuery(it)

            },
            label = { Text(text = stringResource(id = R.string.search)) },
            placeholder = { Text(text = "Search for movies...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (state.searchQuery.isBlank()) {
            LazyColumnListOfMovies(
                movieListViewModel = movieListViewModel,
                navController = navController
            )
        } else {
            LazyColumnForListOfSearchQuery(
                movieListViewModel = movieListViewModel,
                navController = navController,
                searchQuery = state.searchQuery,
            )
        }
    }
}


@Composable
fun LazyColumnListOfMovies(
    navController: NavController,
    movieListViewModel: MovieListViewModel
) {
    val list = movieListViewModel.moviePage.collectAsLazyPagingItems()
    when (val refreshState = list.loadState.refresh) {
        is LoadState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Yellow)
            }
        }

//        is LoadState.Error -> {
//            val message = refreshState.error.message ?: "Something went wrong"
//            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = message, color = Color.Yellow, fontSize = 18.sp)
//            }
//        }

        else -> {

            LazyColumn {
                items(list.itemCount) { item ->
                    list[item]?.let { movieData ->
                        MovieCardView(movieListUi = movieData, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun LazyColumnForListOfSearchQuery(
    movieListViewModel: MovieListViewModel,
    navController: NavController,
    searchQuery: String,
) {

    val searchResults = movieListViewModel.searchPage?.collectAsLazyPagingItems()

    var query by remember {
        mutableStateOf("")
    }
    LaunchedEffect(searchQuery) {
        if (query != searchQuery) {
            searchResults?.refresh()
            query = searchQuery
        } else
            query = searchQuery
    }


//     Check if search results are loaded and update loading state
    if (searchResults?.loadState?.refresh is LoadState.Loading && searchQuery.isBlank()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Yellow)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Searching for \"$searchQuery\"...",
                    color = Color.Yellow,
                    fontSize = 16.sp
                )
            }
        }
    } else if (searchResults?.itemCount == 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No results found for \"$searchQuery\"",
                    color = Color.Yellow,
                    fontSize = 16.sp
                )
            }
        }
    } else {

        LazyColumn {
            items(searchResults!!.itemCount) { index ->
                searchResults[index]?.let { result ->
                    SearchQueryCardView(
                        navController = navController,
                        searchQueryUi = result
                    )
                }
            }

        }
    }
}


