package com.example.moviebrowserapp.mainscreen.domain.searchquerylogic

import android.util.Log
import androidx.room.Dao
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.entity.searchqueryentites.SearchQueryResponse
import com.example.moviebrowserapp.network.CheckNetworkConnection
import com.example.moviebrowserapp.network.Network
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchQueryRepo@Inject constructor(val searchQueryApiServices: SearchQueryApiServices ) {

    suspend fun getSearchQuery(query: String, page: Int): SearchQueryResponse {
        Log.d("SearchRepo", "API call → query=$query page=$page")
            val response = searchQueryApiServices.getSearchQuery(query, page)

            // for debugging
            Log.d("SearchRepo", "API response → ${response.results.size} results")

            return response
    }

}