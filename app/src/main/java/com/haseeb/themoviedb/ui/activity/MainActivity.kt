package com.haseeb.themoviedb.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haseeb.themoviedb.R
import com.haseeb.themoviedb.data.local.MovieDatabase
import com.haseeb.themoviedb.databinding.ActivityMainBinding
import com.haseeb.themoviedb.di.ViewModelFactory
import com.haseeb.themoviedb.ui.fragments.HomeFragment
import com.haseeb.themoviedb.ui.fragments.SearchFragment
import com.haseeb.themoviedb.ui.viewmodels.HomeViewModel
import com.haseeb.themoviedb.utils.AppUtilities
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentDispatchingAndroidInjector
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: ActivityMainBinding
    lateinit var manager: FragmentManager
    lateinit var fragment: Fragment

    val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewmodel = viewModel
        init()
        var movieDatabase = MovieDatabase.getDatabase(applicationContext)
    }
    private fun test(){

    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        manager = supportFragmentManager
        fragment = HomeFragment()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        initFragmentListener()
        binding.ivCloseSearch.setOnClickListener {
            binding.etSearch.text.clear()
            viewModel.resetSearch()
        }
        binding.ivBookmarks.setOnClickListener {
            val intent = Intent(this@MainActivity, BookmarkActivity::class.java)
            startActivity(intent)
        }
        val watcher = object : TextWatcher {
            private var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor)
                    return
                searchFor = searchText

                launch {
                    delay(500)
                    if (searchText != searchFor)
                        return@launch
                    if (searchText.trim().length > 2) {
                        viewModel.resetSearch()
                        viewModel.currentView.value = HomeViewModel.ViewStatus.LOADING
                        viewModel.searchQuery = searchText
                        if (AppUtilities.isNetworkConnected(this@MainActivity)) {
                            viewModel.fetchSearchMovieData()
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }

        binding.etSearch.addTextChangedListener(watcher)
    }

    private fun initFragmentListener() {
        viewModel.currentFragment.observe(this@MainActivity, Observer {
            when (it) {
                "Home" -> initFragment()
                "Search" -> setSearchFragment()
            }
        })
    }

    private fun initFragment() {
        binding.titleLayout.visibility = View.VISIBLE
        binding.searchLayout.visibility = View.GONE
        binding.ivBack.visibility = View.GONE
        if (!fragment.isAdded && fragment is HomeFragment) {
            fragment = HomeFragment()
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.add(R.id.container, fragment, "Home")
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    private fun setSearchFragment() {
        binding.titleLayout.visibility = View.GONE
        binding.searchLayout.visibility = View.VISIBLE
        binding.ivBack.visibility = View.VISIBLE
        fragment = SearchFragment()
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, fragment, "Search")
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onBackPressed() {
        if (fragment.tag == "Home" || fragment.tag == null) {
            finish()
        } else {
            viewModel.currentFragment.value = "Home"
            super.onBackPressed()
        }
    }
}
