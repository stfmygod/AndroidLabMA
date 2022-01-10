package com.faculta.androidlabma.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.faculta.androidlabma.data.db.AppDatabase
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.data.network.ApiService
import com.faculta.androidlabma.helpers.Constants
import com.faculta.androidlabma.helpers.Convertor
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWorker(
    val context: Context,
    val workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val db = AppDatabase.getInstance(context).movieDAO()
        val apiService = ApiService.create()
        val token = inputData.getString(Constants.TOKEN_KEY)

        val moviesList = db.getAllUnsyncedMovies()
        moviesList.forEach {
            if (it._id == null) {
                apiService.addMovie("Bearer $token", Convertor.convertFromMovieDbToMovie(it)).enqueue(object:
                    Callback<Movie> {
                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        val newMovie = response.body()?: Movie()
                        it.isSynced = true
                        it._id = newMovie._id
                        CoroutineScope(Dispatchers.IO).launch {
                            db.updateMovie(it)
                        }
                        Log.d("MyWorker", "Movie sync successful!")
                    }

                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.d("MyWorker", "Movie sync failed: $t")
                    }
                })
            } else {
                val id = it._id
                apiService.updateMovie("Bearer $token", Convertor.convertFromMovieDbToMovie(it), id?: "").enqueue(object:
                    Callback<Movie> {
                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        it.isSynced = true
                        CoroutineScope(Dispatchers.IO).launch {
                            db.updateMovie(it)
                        }
                        Log.d("MyWorker", "Movie sync successful!")
                    }

                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.d("MyWorker", "Movie sync failed: $t")
                    }
                })
            }
        }

        Log.d("MyWorker", "Syncing with server...")
        return Result.retry() // TODO set to success
    }
}