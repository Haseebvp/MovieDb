package com.haseeb.themoviedb.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieObject(
    @PrimaryKey val id: Int,
    val original_title: String,
    val title: String,
    val overview: String,
    val poster_path: String,
    var bookmarked: Boolean
)
