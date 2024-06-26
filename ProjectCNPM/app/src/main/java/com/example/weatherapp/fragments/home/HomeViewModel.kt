package com.example.weatherapp.fragments.home

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CurrentLocation
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.Forecast
import com.example.weatherapp.data.LiveDataEvent
import com.example.weatherapp.data.WeatherData
import com.example.weatherapp.network.repository.WeatherDataRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(private val weatherDataRepository: WeatherDataRepository) :ViewModel() {

    //region Current Location
    private val _currentLocation = MutableLiveData<LiveDataEvent<CurrentLocationDataState>>()
    val currentLocation: LiveData<LiveDataEvent<CurrentLocationDataState>>get() = _currentLocation

    fun getCurentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ){
        viewModelScope.launch {
            emitCurrentLocationUiState(isLoading = true)
            weatherDataRepository.getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onSuccess = { currentLocation ->
                    updateAddressText(currentLocation,geocoder)
                },
                onFailure = {
                    emitCurrentLocationUiState(error = "Unable to fetch current location")
                }
            )
        }
    }
    private fun updateAddressText( currentLocation: CurrentLocation, geocoder: Geocoder){
        viewModelScope.launch {
            runCatching {
                weatherDataRepository.updateAddressText(currentLocation,geocoder)
            }.onSuccess { location ->
                emitCurrentLocationUiState( currentLocation = location)
            }.onFailure {
                emitCurrentLocationUiState(
                    currentLocation = currentLocation.copy(
                        location = "N/A"
                    )
                )
            }
        }
    }

    private fun emitCurrentLocationUiState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? =null,
        error: String? =null
    ){
        val currentLocationDataState = CurrentLocationDataState(isLoading, currentLocation, error)
        _currentLocation.value = LiveDataEvent(currentLocationDataState)
    }


    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )

    // endregion

    private val _weatherData = MutableLiveData<LiveDataEvent<WeatherDataState>>()
    val weatherData: LiveData<LiveDataEvent<WeatherDataState>> get() = _weatherData

    fun getWeatherData(latitude: Double, longitude: Double){
        viewModelScope.launch {
            emitWeatherDataUiState(isLoading = true)
            weatherDataRepository.getWeatherData(latitude,longitude)?.let { weatherData ->
                emitWeatherDataUiState(
                    currentWeather = CurrentWeather(
                        icon = weatherData.current.condition.icon,
                        temperature = weatherData.current.temperature,
                        wind = weatherData.current.wind,
                        humidity = weatherData.current.humidity,
                        chanceOfRain = weatherData.forecast.forecastDay.first().day.chanceOfRain
                    ),
                    //thêm các thuộc tính như: giờ, nhiệt độ, cảm giác như .. độ, icon thời tiết vào hàm getWeatherData
                    forecast = weatherData.forecast.forecastDay.first().hour.map{
                        Forecast(
                            time = getForestcastTime(it.time),
                            temperature = it.temperature,
                            feelslikeTemperature = it.feelsLikeTemperature,
                            icon = it.condition.icon
                        )
                    }

                )
            }?: emitWeatherDataUiState(error = "Unable to fetch weather data")
        }
    }

    private fun emitWeatherDataUiState(
        isLoading: Boolean =false,
        currentWeather: CurrentWeather? = null,
        forecast: List<Forecast>? = null, //thêm danh sác thời tiết-giờ của ngày hiện tại vào giao diện
        error: String? =null
    ){
        val weatherDataState = WeatherDataState(isLoading, currentWeather, forecast, error)
        _weatherData.value = LiveDataEvent(weatherDataState)
    }

    //region Weather Data
    data class WeatherDataState(
        val isLoading: Boolean,
        val currentWeather: CurrentWeather?,
        val forecast: List<Forecast>?, //thêm danh sác thời tiết-giờ của ngày hiện tại vào giao diện
        val error: String?
    )
    // gọi hàm getForestcastTime lấy ngày giờ, tháng năm của ngày hiện tại
    private fun getForestcastTime(dateTime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd HH:mm" , Locale.getDefault())
        val date = pattern.parse(dateTime) ?: return dateTime
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

    }
}