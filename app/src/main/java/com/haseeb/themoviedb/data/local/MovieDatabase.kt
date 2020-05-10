package com.haseeb.themoviedb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haseeb.themoviedb.data.models.MovieObject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [MovieObject::class], version = 1, exportSchema = false)
abstract class MovieDatabase :RoomDatabase(){
    abstract fun movieDao(): MovieDao
    companion object {
        private val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(
            context: Context
        ): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database"
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}