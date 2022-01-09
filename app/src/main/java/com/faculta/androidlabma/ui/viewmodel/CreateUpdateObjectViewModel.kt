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

class CreateUpdateObjectViewModel: ViewModel() {
    private var apiService: ApiService = ApiService.create()
    val errorMessageLiveData = MutableLiveData<String>()
    val canNavigateToNextDestination = MutableLiveData<Boolean>()
    val preNavigationMessage = MutableLiveData<String>()
    private lateinit var db: MovieDAO

    fun setDb(movieDAO: MovieDAO) {
        db = movieDAO
    }

    fun createObject(token: String, movie: MovieDB) {
        viewModelScope.launch {
            if (NetworkUtils.isInternetConnected) {
                apiService.addMovie("Bearer $token", Convertor.convertFromMovieDbToMovie(movie)).enqueue(object : Callback<Movie> {
                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        when (response.code()) {
                            201 -> {
                                canNavigateToNextDestination.value = true
                                preNavigationMessage.value = "Create successful!"
                            }
                            400 -> {
                                errorMessageLiveData.value = "Bad request."
                            }
                            else -> {
                                errorMessageLiveData.value = "There was an error."
                                Log.d("CreateUpdateViewModel", response.message().toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.d("CreateUpdateViewModel", t.message.toString())
                    }
                })
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    movie.isSynced = false
                    db.addMovies(movie)
                    canNavigateToNextDestination.postValue(true)
                    preNavigationMessage.postValue("There is no internet. When the internet is connected the new movie will be synced with the server.")
                }

            }
        }
    }

    fun updateObject(token: String, movie: MovieDB, id: String) {
        viewModelScope.launch {
            if (NetworkUtils.isInternetConnected) {
                apiService.updateMovie("Bearer $token", Convertor.convertFromMovieDbToMovie(movie), id).enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.d("CreateUpdateViewModel", t.message.toString())
                    }

                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        when (response.code()) {
                            200 -> {
                                canNavigateToNextDestination.value = true
                                preNavigationMessage.value = "Update successful!"
                            }
                            400 -> {
                                errorMessageLiveData.value = "Bad request."
                            }
                            405 -> {
                                errorMessageLiveData.value = "Resource does not exist anymore."
                            }
                            else -> {
                                errorMessageLiveData.value = "There was an error."
                                Log.d("CreateUpdateViewModel", response.message().toString())
                            }
                        }
                    }

                })
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    movie.isSynced = false
                    db.updateMovie(movie)
                    canNavigateToNextDestination.postValue(true)
                    preNavigationMessage.postValue("There is no internet. When the internet is connected the new movie will be synced with the server.")
                }
            }
        }
    }

    fun deleteMovie(token: String, id: String) {
        viewModelScope.launch {
            if (NetworkUtils.isInternetConnected) {
                apiService.deleteMovie("Bearer $token", id).enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        when (response.code()) {
                            204 -> {
                                canNavigateToNextDestination.value = true
                                preNavigationMessage.value = "Delete successful!"
                            }
                            403 -> {
                                errorMessageLiveData.value = "Forbidden."
                            }
                            else -> {
                                errorMessageLiveData.value = "There was an error."
                                Log.d("CreateUpdateViewModel", response.message().toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.d("CreateUpdateViewModel", t.message.toString())
                    }
                })
            } else {
                preNavigationMessage.value = "Delete cannot be done because you are not connected to the internet."
            }
        }
    }
}