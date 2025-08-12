package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListUi
import com.example.moviebrowserapp.network.CheckNetworkConnection
import kotlinx.coroutines.flow.map

class MoviePaginationImpl(
    val movieListUseCase: MovieListUseCase,
    val networkConnectionction: CheckNetworkConnection,
    val dao: MovieFirstListDao
) : PagingSource<Int, MovieListUi>() {

    override fun getRefreshKey(state: PagingState<Int, MovieListUi>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)

            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListUi> {
        Log.d("PagingDebug", "Loading page ${params.key} with loadSize ${params.loadSize}")


        kotlin.runCatching {
            val page = params.key ?: 1
            if (page == 1 && !networkConnectionction.isOnline()) {
                val list = dao.getAllMovies()
                val show = list.map { i ->
                    MovieListUi(
                        id = i.id,
                        title = i.title,
                        poster_path = i.poster_path
                    )
                }
                return LoadResult.Page(
                    data = show,
                    prevKey = null,
                    nextKey = null
                )
            } else {
                val response = movieListUseCase.callMovieList(page)
                dao.insertAll(response.movieList.map { i ->
                    MovieEntity(
                        id = i.id,
                        title = i.title,
                        poster_path = i.poster_path
                    )
                })
                if (response.movieList.isEmpty()) {
                    return LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                } else {
                    return LoadResult.Page(
                        data = response.movieList,
                        prevKey = null,
                        nextKey = if (response.movieList.size < 20) null else page + 1
                    )
                }
            }
        }.onFailure {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = arrayListOf(),
            prevKey = null,
            nextKey = null
        )

    }

}