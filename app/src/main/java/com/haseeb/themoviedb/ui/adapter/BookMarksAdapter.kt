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

class BookMarksAdapter(private val context: Context, private var list: List<MovieObject?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieHolder(view, context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setList(list: ArrayList<MovieObject?>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        list.get(position)?.let { (holder as MovieHolder).bind(it) }

    }

    class MovieHolder(
        var itemview: View,
        var context: Context
    ) : RecyclerView.ViewHolder(itemview) {
        fun bind(item: MovieObject) {
            itemview.bookmark.visibility = View.GONE
            itemview.title.text = item.title
            itemview.desc.text = item.overview
            Glide.with(context).load(AppConstants.IMAGE_BASE + item.poster_path)
                .into(itemview.poster)

        }
    }

}