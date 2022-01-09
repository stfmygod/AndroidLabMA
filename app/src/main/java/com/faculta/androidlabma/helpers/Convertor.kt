package com.faculta.androidlabma.helpers

import com.faculta.androidlabma.data.db.model.MovieDB
import com.faculta.androidlabma.data.models.Movie

object Convertor {
    fun convertFromMovieDbToMovie(movies: List<MovieDB>): List<Movie> {
        val newList = arrayListOf<Movie>()
        movies.forEach {
            newList.add(Movie(it._id, it.name, it.date, it.rating, it.isSeenAtCinema, it.photoPath))
        }
        return newList
    }

    fun convertFromMovieDbToMovie(movie: MovieDB): Movie {
        return Movie(movie._id, movie.name, movie.date, movie.rating, movie.isSeenAtCinema, movie.photoPath)
    }

    fun convertFromMovieToMovieDb(movies: List<Movie>, isSynced: Boolean): List<MovieDB> {
        val newList = arrayListOf<MovieDB>()
        movies.forEach {
            newList.add(MovieDB(_id = it._id?: "", name = it.name, date = it.date, rating = it.rating, isSeenAtCinema = it.isSeenAtCinema, photoPath = it.photoPath, isSynced = isSynced))
        }
        return newList
    }

    fun convertFromMovieToMovieDb(movie: Movie, isSynced: Boolean): MovieDB {
        return MovieDB(_id = movie._id?: "", name = movie.name, date = movie.date, rating = movie.rating, isSeenAtCinema = movie.isSeenAtCinema, photoPath = movie.photoPath, isSynced = isSynced)
    }
}