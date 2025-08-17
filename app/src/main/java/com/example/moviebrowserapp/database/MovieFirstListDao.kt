package com.example.moviebrowserapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieFirstListDao {

    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies(): List<MovieEntity>


    @Upsert
    suspend fun insertAll(movies: List<MovieEntity>)
}