package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListUi
import com.example.moviebrowserapp.network.CheckNetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviePaginationImpl(
    val useCase: MovieListUseCase,
    val checkNetworkConnection: CheckNetworkConnection,
) : PagingSource<Int, MovieListUi>() {

    override fun getRefreshKey(state: PagingState<Int, MovieListUi>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListUi> = withContext(Dispatchers.IO) {

        if (!checkNetworkConnection.isOnline()) {
            return@withContext LoadResult.Error(Exception("No internet connection"))
        }

        try {
            val page = params.key ?: 1
            Log.d("PagingDebug", "Calling useCase for page: $page")

            val response = useCase.callMovieList(page)
            Log.d("PagingDebug", "Received response with ${response.movieList.size} movies")


            return@withContext LoadResult.Page(
                data = response.movieList,
                prevKey =  null,
                nextKey = if (response.movieList.size < 20) null else page + 1            )
        } catch (e: Exception) {
            Log.e("PagingDebug", "Error loading page ${params.key}: ${e.message}", e)
            return@withContext LoadResult.Error(e)
        }
    }
}