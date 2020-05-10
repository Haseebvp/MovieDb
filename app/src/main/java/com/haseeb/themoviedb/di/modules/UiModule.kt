package com.haseeb.themoviedb.di.modules

import com.haseeb.themoviedb.ui.activity.BookmarkActivity
import com.haseeb.themoviedb.ui.activity.MainActivity
import com.haseeb.themoviedb.ui.fragments.HomeFragment
import com.haseeb.themoviedb.ui.fragments.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {

    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributesBookmarkActivity(): BookmarkActivity

    @ContributesAndroidInjector
    abstract fun contributesHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributesSearchFragment(): SearchFragment

}