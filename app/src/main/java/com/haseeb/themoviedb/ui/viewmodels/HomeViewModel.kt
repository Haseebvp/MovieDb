package com.haseeb.themoviedb.ui.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haseeb.themoviedb.data.local.MovieDao
import com.haseeb.themoviedb.data.local.MovieDatabase
import com.haseeb.themoviedb.data.local.MovieRepository
import com.haseeb.themoviedb.data.models.MovieObject
import com.haseeb.themoviedb.data.network.ApiService
import com.haseeb.themoviedb.di.modules.DatabaseModule
import com.haseeb.themoviedb.utils.AppConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel @Inject constructor(var apiService: ApiService, var application: Application) : ViewModel() {

    var totalPages = 0
    var page: Int = 1
    var movieList: ArrayList<MovieObject?> = ArrayList()


    var currentView: MutableLiveData<ViewStatus> = MutableLiveData()
    var currentFragment: MutableLiveData<String> = MutableLiveData()

    var searchQuery: String = ""
    var searchMovieList: ArrayList<MovieObject?> = ArrayList()
    var search_totalPages = 0
    var search_page: Int = 1
    var movieRepository:MovieRepository? = null

    init {
        currentFragment.value = "Home"
        currentView.value = ViewStatus.LOADING
        movieRepository = MovieRepository(application)
    }

    fun fetchMovieData(isRefresh: Boolean) {
        apiService.getMovieData(AppConstants.API_KEY, page).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                if (it != null) {
                    totalPages = it.total_pages
                    if (isRefresh) {
                        movieList = it.results
                    } else {
                        if (page > 1 && movieList.contains(null)) {
                            movieList.removeAt(movieList.size - 1)
                        }
                        movieList.addAll(it.results)
                    }
                    currentView.value = ViewStatus.SUCCESS
                } else {
                    currentView.value = ViewStatus.ERROR
                }
            }, Consumer {
                print("issuessssssssssss")
            })
    }

    fun fetchSearchMovieData() {
        apiService.getSearchMovieData(AppConstants.API_KEY, search_page, searchQuery).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                if (it != null) {
                    search_totalPages = it.total_pages
                    if (search_page > 1 && searchMovieList.contains(null)) {
                        searchMovieList.removeAt(searchMovieList.size - 1)
                        searchMovieList.addAll(it.results)
                    } else {
                        searchMovieList = it.results
                    }
                    currentView.value = ViewStatus.SUCCESS
                } else {
                    currentView.value = ViewStatus.ERROR
                }
            },
                Consumer {
                print("issuessssssssssss")
            })

    }

    fun searchClick() {
        currentFragment.value = "Search"
    }

    enum class ViewStatus {
        LOADING, SUCCESS, ERROR, LOADMORE
    }

    fun resetSearch(){
        searchMovieList = ArrayList()
        search_page = 1
        searchQuery = ""
        currentView.value = ViewStatus.SUCCESS
    }
}