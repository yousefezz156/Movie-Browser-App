package com.example.moviebrowserapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val idFake:Int=1,
    val id:Int,
    val poster_path : String?,
    val title:String
)
