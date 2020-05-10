package com.haseeb.themoviedb.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.haseeb.themoviedb.data.models.MovieObject


@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun loadBookmarks(): LiveData<List<MovieObject?>>?

    @Insert
    fun insert(movie: MovieObject)

    @Delete
    fun delete(movie: MovieObject)
}