package com.faculta.androidlabma.ui.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.faculta.androidlabma.R
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.databinding.ViewHolderMovieBinding
import com.faculta.androidlabma.helpers.DateUtils
import java.util.*

class ObjectListViewHolder(private val binding: ViewHolderMovieBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindData(item: Movie, context: Context) {
        binding.nameTextView.text = context.getString(R.string.name_string, item.name?: "")
        binding.ratingTextView.text = context.getString(R.string.rate_string, item.rating?: "")

        if (item.isSeenAtCinema == true) {
            binding.seenInCinemaImageView.visibility = View.VISIBLE
        } else {
            binding.seenInCinemaImageView.visibility = View.GONE
        }

        Glide.with(context)
            .load(item.photoPath)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .fallback(R.drawable.image_placeholder)
            .into(binding.movieImage)

        binding.dateTextView.text = context.getString(R.string.date_string, DateUtils.getStringFromDate(item.date?: Date(System.currentTimeMillis())))
    }

}