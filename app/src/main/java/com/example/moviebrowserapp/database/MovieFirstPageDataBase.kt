package com.example.moviebrowserapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 2, exportSchema = false)
abstract class MovieFirstPageDataBase:RoomDatabase() {
    abstract fun getMovieFirstListDao(): MovieFirstListDao
}