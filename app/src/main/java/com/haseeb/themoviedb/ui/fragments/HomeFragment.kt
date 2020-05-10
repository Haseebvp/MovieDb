package com.haseeb.themoviedb.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.haseeb.themoviedb.R
import com.haseeb.themoviedb.databinding.FragmentHomeBinding
import com.haseeb.themoviedb.di.ViewModelFactory
import com.haseeb.themoviedb.ui.adapter.MovieAdapter
import com.haseeb.themoviedb.ui.viewmodels.HomeViewModel
import com.haseeb.themoviedb.utils.AppUtilities
import com.haseeb.themoviedb.utils.PaginationListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: FragmentHomeBinding
    lateinit var lm: LinearLayoutManager
    lateinit var adapter: MovieAdapter

    val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        binding.viewmodel = viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.movielist.setHasFixedSize(true)
        lm = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(
            this.context,
            DividerItemDecoration.VERTICAL
        )
        binding.movielist.addItemDecoration(decoration)
        binding.movielist.layoutManager = lm
        if (this.context != null) {
            adapter = MovieAdapter(viewModel,this.context!!, viewModel.movieList)
            binding.movielist.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.page = 1
            if (AppUtilities.isNetworkConnected(this.context!!)) {
                viewModel.fetchMovieData(true)
            }
        }

        val listener:PaginationListener = object: PaginationListener(lm){
            override fun loadMoreItems() {
                viewModel.currentView.value = HomeViewModel.ViewStatus.LOADING
                viewModel.movieList.add(null)
                adapter.notifyItemInserted(viewModel.movieList.size - 1)
                viewModel.page += 1
                if (AppUtilities.isNetworkConnected(context!!)) {
                    viewModel.fetchMovieData(false)
                }
            }

            override fun isLastPage(): Boolean {
                return viewModel.page == viewModel.totalPages
            }

            override fun isLoading(): Boolean {
                return viewModel.currentView.value == HomeViewModel.ViewStatus.LOADING
            }

        }
        binding.movielist.addOnScrollListener(listener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
        if (AppUtilities.isNetworkConnected(context!!)) {
            viewModel.fetchMovieData(false)
        }
    }

    private fun observeData() {
        viewModel.currentView.observe(viewLifecycleOwner, Observer {
            when (it) {
                HomeViewModel.ViewStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (binding.swiperefresh.isRefreshing){
                        binding.swiperefresh.isRefreshing = false
                    }
                    adapter.notifyDataSetChanged()
                }
                HomeViewModel.ViewStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                }
                else -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }

        })
    }

}