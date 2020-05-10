package com.haseeb.themoviedb.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.haseeb.themoviedb.data.models.MovieObject
import java.lang.Exception


class MovieRepository (var application: Application) {
    private var movieDao: MovieDao? = null
    private var movieList: LiveData<List<MovieObject>>? = null

    init {
        val db: MovieDatabase = MovieDatabase.getDatabase(application)
        movieDao = db.movieDao()
    }

    fun getAllMovies(): LiveData<List<MovieObject?>>? {
        return movieDao?.loadBookmarks()
    }

    fun insert(movie: MovieObject?) {
        MovieDatabase.databaseWriteExecutor.execute {
            try {
                movie?.let { movieDao?.insert(it) }
            } catch (e: Exception) {

            }
        }
    }


    fun delete(item: MovieObject) {
        MovieDatabase.databaseWriteExecutor.execute {
            try {
                movieDao?.delete(movie = item)
            } catch (e: Exception) {

            }
        }
    }
}
