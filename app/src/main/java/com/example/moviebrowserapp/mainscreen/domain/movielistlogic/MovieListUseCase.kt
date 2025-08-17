package com.example.moviebrowserapp.mainscreen.domain.movielistlogic

import android.util.Log
import com.example.moviebrowserapp.database.MovieEntity
import com.example.moviebrowserapp.database.MovieFirstListDao
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.MovieBody
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListPaginationResponse
import com.example.moviebrowserapp.mainscreen.entity.movielistentites.movielistui.MovieListUi
import javax.inject.Inject

class MovieListUseCase @Inject constructor(val movieListRepository: MovieListRepository ){

    suspend fun callMovieList(page :Int) : MovieListPaginationResponse {

         val modalList = ArrayList<MovieListUi>()
        val response = movieListRepository.getMovieList(page)

        response.results.map { modalList.add(it.mapIt()) }

        return MovieListPaginationResponse(
            page = page,
            movieList = modalList
        )

    }

    suspend fun addMovieToDatabase(dao:MovieFirstListDao){

        val response = movieListRepository.getMovieList(1)
        val modalList = ArrayList<MovieEntity>()
        response.results.map {
            modalList.add(it.mapItDataBase())

        }


        dao.insertAll(modalList)

        Log.d("DBuseCase", "total movies added success: ${dao.getAllMovies().size} && ${modalList.size}")
    }


    fun MovieBody.mapIt(): MovieListUi {
        return MovieListUi(
            id = id,
            poster_path = poster_path,
            title = title
        )
    }
    fun MovieBody.mapItDataBase():MovieEntity{
        return MovieEntity(
            idFake = id,
            id = id,
            poster_path = poster_path,
            title = title
        )
    }

    fun MovieEntity.toUiModel(): MovieListUi {
        return MovieListUi(
            id = id,
            title = title,
            poster_path =  poster_path
        )
    }

    fun List<MovieEntity>.toUiList(): List<MovieListUi> {
        return map { it.toUiModel() }
    }
}