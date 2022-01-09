package com.faculta.androidlabma.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faculta.androidlabma.data.db.dao.MovieDAO
import com.faculta.androidlabma.data.db.model.MovieDB
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.data.network.ApiService
import com.faculta.androidlabma.helpers.Convertor
import com.faculta.androidlabma.helpers.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ObjectListViewModel() : ViewModel() {
    private val apiService: ApiService = ApiService.create()
    val moviesListLiveData = MutableLiveData<List<MovieDB>>()
    val errorMessageLiveData = MutableLiveData<String>()
    private lateinit var db: MovieDAO

    fun setDb(movieDAO: MovieDAO) {
        db = movieDAO
    }


    fun getMovies(token: String) {
        viewModelScope.launch {
            if (NetworkUtils.isInternetConnected) {
                apiService.getMovies("Bearer $token")
                    .enqueue(object : Callback<List<Movie>> {
                        override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                            errorMessageLiveData.value = t.message
                            Log.d("ObjectListViewModel", "$t")
                        }

                        override fun onResponse(
                            call: Call<List<Movie>>,
                            response: Response<List<Movie>>
                        ) {
                            if (response.code() == 200) {
                                viewModelScope.launch(Dispatchers.IO) {
                                    db.nukeMovies()
                                    db.addMovies(*Convertor.convertFromMovieToMovieDb(response.body()?: listOf(), true).toTypedArray())
                                    moviesListLiveData.postValue(db.getAllMovies())
                                }
                            } else {
                                errorMessageLiveData.value = "There was an error."
                            }
                        }
                    })
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    moviesListLiveData.postValue(db.getAllMovies())
                }
            }

        }
    }

}