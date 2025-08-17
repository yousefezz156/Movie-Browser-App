package com.example.moviebrowserapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey
    val idFake:Int,
    val id:Int,
    val poster_path : String?,
    val title:String
)
