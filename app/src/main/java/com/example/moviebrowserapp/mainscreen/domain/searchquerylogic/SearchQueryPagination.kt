package com.example.moviebrowserapp.mainscreen.domain.searchquerylogic

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.searchqueryUi.SearchQueryUi
import com.example.moviebrowserapp.network.CheckNetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchQueryPaginationImpl @Inject constructor(val useCase: SearchQueryUseCase, val query: String, val checkNetworkConnection: CheckNetworkConnection): PagingSource<Int, SearchQueryUi>() {

    override fun getRefreshKey(state: PagingState<Int, SearchQueryUi>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchQueryUi> {
        if (!checkNetworkConnection.isOnline()) {
            return LoadResult.Error(Exception("No internet connection"))
        }
        try {
            val page = params.key ?: 1

            Log.d("SearchPaging", "Loading page $page with query '$query'")
            val response = useCase.callSearchQuery(query, page)
            

            return LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (response.results.size<20) page + 1 else null
            )
        } catch (e: Exception) {
            Log.e("SearchPaging", "Error loading page ${params.key}: ${e.message}", e)
            return LoadResult.Error(e)
        }
    }
}