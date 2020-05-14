package com.haseeb.themoviedb.di.modules

import android.app.Application
import com.haseeb.themoviedb.data.local.MovieDao
import com.haseeb.themoviedb.data.local.MovieDatabase
import com.haseeb.themoviedb.data.local.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDateBase(application: Application): MovieDatabase {
        return MovieDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }


    @Singleton
    @Provides
    fun provideMovieRepository(application: Application): MovieRepository {
        return MovieRepository(application)
    }
}