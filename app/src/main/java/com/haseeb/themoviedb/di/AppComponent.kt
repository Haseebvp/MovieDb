package com.haseeb.themoviedb.di

import android.app.Application
import com.haseeb.themoviedb.MovieApplication
import com.haseeb.themoviedb.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidInjectionModule::class,
    AppModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    UiModule::class,
    DatabaseModule::class])
interface AppComponent{
    fun inject(app: MovieApplication)
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}