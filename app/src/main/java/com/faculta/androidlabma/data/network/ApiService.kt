package com.faculta.androidlabma.data.network

import com.faculta.androidlabma.data.models.LoginRequest
import com.faculta.androidlabma.data.models.LoginResponse
import com.faculta.androidlabma.data.models.Movie
import com.faculta.androidlabma.data.models.RegisterRequest
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    companion object {
        var BASE_URL = "http://192.168.73.5:3000/"

        fun create() : ApiService {

            val gson = GsonBuilder().setDateFormat("dd-MM-yyyy'T'HH:mm:ss").create()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiService::class.java)

        }
    }


    @GET("/api/item")
    fun getMovies(
        @Header("Authorization") token: String
    ): Call<List<Movie>>

    @POST("api/item")
    fun addMovie(
        @Header("Authorization") token: String,
        @Body movie: Movie
    ): Call<Unit>

    @PUT("api/item/{id}")
    fun updateMovie(
        @Header("Authorization") token: String,
        @Body movie: Movie,
        @Path("id") id: String
    ): Call<Unit>

    @DELETE("api/item/{id}")
    fun deleteMovie(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<Unit>

    @POST("api/auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ) : Call<LoginResponse>

    @POST("api/auth/signup")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<Unit>




}