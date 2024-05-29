package com.example.weatherapp.network.api

import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.data.RemoteWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object {
        const val BASE_URL ="https://api.weatherapi.com/v1/"
        const val API_KEY ="29931f8e9e1745d6b3882750241005"
    }
    @GET("search.json")
    suspend fun searchLocation(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<List<RemoteLocation>>

    @GET("forecast.json")

    suspend fun getWeatherData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<RemoteWeatherData>
}