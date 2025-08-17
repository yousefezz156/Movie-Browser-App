package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import android.util.Log
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieListResponse
import com.example.moviebrowserapp.network.Network
import com.example.moviebrowserapp.network.httpsurlconnection.MovieHttpsUrlConnection
import com.example.moviebrowserapp.network.httpsurlconnection.mapperJson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListRepository@Inject constructor( val api : MovieHttpsUrlConnection/*val movieListApiServices: MovieListApiServices*/ ) {

//    suspend fun getMovieList(page :Int ): MoiveListResponse {
//
//
//        val response= movieListApiServices.MovieList(page)
//            return response
//    }
    suspend fun getMovieList(page :Int ): MovieListResponse {
    Log.d("RepositoryDebug", "Calling API for page: $page")
    val response = api.fetchMovies(page)
    if (response == null) {
    }
    return mapperJson(response)
}

//    suspend fun getSearchQueryFromDB(): List<MovieEntity> {
//        return dao.getAllMovies()
//    }

}