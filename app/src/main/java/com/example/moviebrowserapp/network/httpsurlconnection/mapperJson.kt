package com.example.moviebrowserapp.network.httpsurlconnection

import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieBody
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

suspend fun mapperJson(response :String) : MovieListResponse = withContext(Dispatchers.IO){

    val json = JSONObject(response)
    val results = json.getJSONArray("results")
    val movieList = mutableListOf<MovieBody>()

    for(i in 0 until results.length()){
        val movieJson = results.getJSONObject(i)
        val movie =MovieBody(
            id = movieJson.getInt("id"),
            poster_path = movieJson.getString("poster_path"),
            title = movieJson.getString("title")
        )
        movieList.add(movie)
    }

    return@withContext MovieListResponse(
        page = json.getInt("page"),
        results = movieList,
        total_page = json.getInt("total_pages"),
        total_results = json.getInt("total_results")

    )
}