package com.faculta.androidlabma.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.faculta.androidlabma.data.db.model.MovieDB
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.databinding.ViewHolderMovieBinding

class ObjectListAdapter(
    private val context: Context,
    private var movies: List<MovieDB>,
    private var onClick: (MovieDB) -> Unit
): RecyclerView.Adapter<ObjectListViewHolder>() {
    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ObjectListViewHolder, position: Int) {
        val item = movies[position]
        holder.itemView.setOnClickListener {
            onClick.invoke(item)
        }
        holder.bindData(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectListViewHolder {
        return ObjectListViewHolder(ViewHolderMovieBinding.inflate(LayoutInflater.from(context), parent, false))
    }
}