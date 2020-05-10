package com.haseeb.themoviedb.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haseeb.themoviedb.R
import com.haseeb.themoviedb.data.models.MovieObject
import com.haseeb.themoviedb.ui.viewmodels.HomeViewModel
import com.haseeb.themoviedb.utils.AppConstants
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter (val viewModel: HomeViewModel, private val context: Context, private var list: ArrayList<MovieObject?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_VIEW = 1
        const val ITEM_PROGRESS = 2
    }

    var itemType = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW) {
            itemType = ITEM_VIEW
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            MovieHolder(view,context,viewModel)
        }else{
            itemType = ITEM_PROGRESS
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress, parent, false)
            ProgressHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.get(position) == null){
            ITEM_PROGRESS
        } else {
            ITEM_VIEW
        }
    }

    fun setList(list: ArrayList<MovieObject?>){
        this.list = list
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemType == ITEM_VIEW){
            list.get(position)?.let { (holder as MovieHolder).bind(it) }
        }
    }

    class MovieHolder(
        var itemview: View,
        var context: Context,
        var viewModel: HomeViewModel
    ) : RecyclerView.ViewHolder(itemview) {
        fun bind(item:MovieObject){
            itemview.title.text = item.title
            itemview.desc.text = item.overview
            Glide.with(context).load(AppConstants.IMAGE_BASE+item.poster_path).into(itemview.poster)
            if (item.bookmarked){
                itemview.bookmark.setImageResource(R.drawable.ic_bookmarked)
            } else {
                itemview.bookmark.setImageResource(R.drawable.ic_bookmark)
            }
            itemview.bookmark.setOnClickListener {
                item.bookmarked = !item.bookmarked
                if (item.bookmarked){
                    itemview.bookmark.setImageResource(R.drawable.ic_bookmarked)
                    viewModel.movieRepository?.insert(item)
                } else {
                    itemview.bookmark.setImageResource(R.drawable.ic_bookmark)
                    viewModel.movieRepository?.delete(item)
                }
            }
        }
    }
    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}