package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MoiveListResponse
import com.example.moviebrowserapp.network.Network
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieListRepository@Inject constructor(val dao: MovieFirstListDao,val movieListApiServices: MovieListApiServices ) {

    suspend fun getMovieList(page :Int ): MoiveListResponse {


        val response= movieListApiServices.MovieList(page)

        dao.insertAll(response.results.map { i ->
            MovieEntity(
                id = i.id,
                title = i.title,
                poster_path = i.poster_path,
            )
        }
        )
            return response
    }

    suspend fun getSearchQueryFromDB(): List<MovieEntity> {
        return dao.getAllMovies()
    }

}