package com.faculta.androidlabma.data.db.dao

import androidx.room.*
import com.faculta.androidlabma.data.db.model.MovieDB

@Dao
interface MovieDAO {
    @Query("SELECT * FROM moviedb")
    fun getAllMovies(): List<MovieDB>

    @Query("SELECT * FROM moviedb WHERE is_synced=0")
    fun getAllUnsyncedMovies(): List<MovieDB>

    @Insert
    fun addMovies(vararg movie: MovieDB)

    @Delete
    fun deleteMovie(movie: MovieDB)

    @Query("DELETE FROM moviedb")
    fun nukeMovies()

    @Update
    fun updateMovie(movie: MovieDB)

}