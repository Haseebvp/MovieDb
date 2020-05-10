package com.haseeb.themoviedb.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.haseeb.themoviedb.data.local.MovieRepository
import com.haseeb.themoviedb.data.models.MovieObject
import com.haseeb.themoviedb.data.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkViewmodel @Inject constructor(application: Application): ViewModel(){

    var bookmarkList:LiveData<List<MovieObject?>>? = null
    var movieRepository: MovieRepository = MovieRepository(application)

    fun fetch(){
        bookmarkList = movieRepository.getAllMovies()
    }

    fun getAllMovies(): LiveData<List<MovieObject?>>? {
        return bookmarkList
    }
}