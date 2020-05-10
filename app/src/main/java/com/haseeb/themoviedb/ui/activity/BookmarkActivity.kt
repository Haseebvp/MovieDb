package com.haseeb.themoviedb.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.haseeb.themoviedb.R
import com.haseeb.themoviedb.data.models.MovieObject
import com.haseeb.themoviedb.databinding.ActivityBookmarkBinding
import com.haseeb.themoviedb.di.ViewModelFactory
import com.haseeb.themoviedb.ui.adapter.BookMarksAdapter
import com.haseeb.themoviedb.ui.adapter.MovieAdapter
import com.haseeb.themoviedb.ui.viewmodels.BookmarkViewmodel
import dagger.android.AndroidInjection
import javax.inject.Inject

class BookmarkActivity : AppCompatActivity(){

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: ActivityBookmarkBinding
    lateinit var adapter: BookMarksAdapter
    var list :ArrayList<MovieObject?> = ArrayList()

    val viewModel: BookmarkViewmodel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(BookmarkViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmark)
        binding.viewmodel = viewModel
        viewModel.fetch()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.bookmarkslistview.setHasFixedSize(true)
        val lm = LinearLayoutManager(this@BookmarkActivity, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(
            this@BookmarkActivity,
            DividerItemDecoration.VERTICAL
        )
        binding.bookmarkslistview.addItemDecoration(decoration)
        binding.bookmarkslistview.layoutManager = lm
        if (this@BookmarkActivity != null) {
            adapter = BookMarksAdapter(this@BookmarkActivity, list)
            binding.bookmarkslistview.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        viewModel.getAllMovies()?.observe(this, Observer {
            if (it != null){
                list.addAll(ArrayList(it))
                adapter.notifyDataSetChanged()
            }
        })
    }
}