package com.faculta.androidlabma.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.data.network.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUpdateObjectViewModel: ViewModel() {
    private var apiService: ApiService = ApiService.create()
    val errorMessageLiveData = MutableLiveData<String>()
    val canNavigateToNextDestination = MutableLiveData<Boolean>()
    val preNavigationMessage = MutableLiveData<String>()

    fun createObject(token: String, movie: Movie) {
        viewModelScope.launch {
            apiService.addMovie("Bearer $token", movie).enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    when(response.code()) {
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

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("CreateUpdateViewModel", t.message.toString())
                }
            })
        }
    }

    fun updateObject(token: String, movie: Movie, id: String) {
        viewModelScope.launch {
            apiService.updateMovie("Bearer $token", movie, id).enqueue(object: Callback<Unit> {
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
        }
    }

    fun deleteMovie(token: String, id: String) {
        viewModelScope.launch {
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
        }
    }
}