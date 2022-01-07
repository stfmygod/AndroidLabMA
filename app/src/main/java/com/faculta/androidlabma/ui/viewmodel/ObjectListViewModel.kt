package com.faculta.androidlabma.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.data.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ObjectListViewModel : ViewModel() {
    private val apiService: ApiService = ApiService.create()
    val moviesListLiveData = MutableLiveData<List<Movie>>()
    val errorMessageLiveData = MutableLiveData<String>()


    fun getMovies(token: String) {
        viewModelScope.launch {
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
                            moviesListLiveData.value = response.body() ?: listOf()
                        } else {
                            errorMessageLiveData.value = "There was an error."
                        }
                    }

                })
        }
    }

}