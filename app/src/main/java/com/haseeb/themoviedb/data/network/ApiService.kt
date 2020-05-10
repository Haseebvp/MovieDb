package com.haseeb.themoviedb.data.network

import com.haseeb.themoviedb.data.models.MovieResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("movie/now_playing")
    fun getMovieData(@Query("api_key") api_key: String, @Query("page") page: Int): Flowable<MovieResponse>


    @GET("search/movie")
    fun getSearchMovieData(@Query("api_key") api_key: String, @Query("page") page: Int, @Query("query") query: String): Flowable<MovieResponse>
}