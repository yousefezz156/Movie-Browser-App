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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            MainScreen(
                movieListViewModel = movieListViewModel,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    movieListViewModel: MovieListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by movieListViewModel.state.collectAsState()

    val movie = movieListViewModel.moviePage.collectAsLazyPagingItems()
    val isRefreshing = movie.loadState.refresh is LoadState.Loading

    val coroutineScope = rememberCoroutineScope()

//    // Check network status when screen is displayed
//    LaunchedEffect(Unit) {
//        movieListViewModel.events(MainScreenIntent.CheckNetworkConnection)
//    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                movieListViewModel.events(MainScreenIntent.RefreshScreen)
            }
        }
    ) {
        if (state.isOffline) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "No Network Connection", color = Color.Red, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(state.movieListOffline) { movie ->
                        MovieCard(
                            navController = navController,
                            id = movie.id,
                            posterPath = movie.poster_path,
                            title = movie.title,
                            height = 190.dp,
                            width = 140.dp
                            )
                    }
                }
            }

        } else {

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {
                // Show offline status if applicable
                if (state.error != null && state.error!!.contains("offline")) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Yellow.copy(alpha = 0.2f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error!!,
                            color = Color.Yellow,
                            fontSize = 14.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = {
                        movieListViewModel.events(MainScreenIntent.SearchQuery(it))
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
                        MovieCard(
                            navController = navController,
                            id = movieData.id,
                            posterPath = movieData.poster_path,
                            title = movieData.title,
                            height = 190.dp,
                            width = 140.dp
                        )
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
    val searchResults = movieListViewModel.searchPage.collectAsLazyPagingItems()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank() && searchResults != null) {
            // Only refresh if we don't have results yet or if the query actually changed
            if (searchResults.itemCount == 0 || searchResults.loadState.refresh !is LoadState.NotLoading) {
                searchResults.refresh()
            }
        }
    }

    // Handle case when searchResults is null (search page not created yet)
    if (searchResults == null) {
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
                    text = "Preparing search for \"$searchQuery\"...",
                    color = Color.Yellow,
                    fontSize = 16.sp
                )
            }
        }
        return
    }

    // Check if search results are loaded and update loading state
    if (searchResults.loadState.refresh is LoadState.Loading && searchResults.itemCount == 0) {
        // Only show loading if we don't have any results yet
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
    } else if (searchResults.itemCount == 0) {
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Try different keywords or check spelling",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    } else {
        LazyColumn {
            items(searchResults.itemCount) { index ->
                searchResults[index]?.let { result ->
                    MovieCard(
                        navController = navController,
                        id = result.id,
                        posterPath = result.poster_path,
                        title = result.title,
                        height = 120.dp,
                        width = 120.dp
                    )
                }
            }

            // Show loading indicator for pagination
            if (searchResults.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Yellow)
                    }
                }
            }
        }
    }
}


