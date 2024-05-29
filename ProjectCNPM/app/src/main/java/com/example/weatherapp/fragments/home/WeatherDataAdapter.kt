package com.example.weatherapp.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherapp.data.CurrentLocation
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.Forecast
import com.example.weatherapp.data.WeatherData
import com.example.weatherapp.databinding.ItemContainerCurrentLocationBinding
import com.example.weatherapp.databinding.ItemContainerCurrentWeatherBinding
import com.example.weatherapp.databinding.ItemContainerForecastBinding
import okhttp3.internal.notify

import org.koin.core.component.getScopeId

class WeatherDataAdapter (
    private val onLocationClicked:() -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private companion object{
        const val INDEX_CURRENT_LOCATION =0
        const val INDEX_CURRENT_WEATHER =1
        const val INDEX_FORECAST = 2 //của tuiiii
    }

    private val weatherData = mutableListOf<WeatherData>()


    fun setCurrentLocation(currentLocation: CurrentLocation){
        if (weatherData.isEmpty()){
            weatherData.add(INDEX_CURRENT_LOCATION, currentLocation)
            notifyItemChanged(INDEX_CURRENT_LOCATION)
        }else{
            weatherData[INDEX_CURRENT_LOCATION] = currentLocation
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        }
    }

    fun setCurrentWeather(currentWeather: CurrentWeather){
        if (weatherData.getOrNull(INDEX_CURRENT_WEATHER)  != null){
            weatherData[INDEX_CURRENT_WEATHER] = currentWeather
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        }else{
            weatherData.add(INDEX_CURRENT_WEATHER, currentWeather)
            notifyItemChanged(INDEX_CURRENT_WEATHER)
        }
    }

    //của tuiiii
    fun setForecastData(forecasst: List<Forecast>){
        weatherData.removeAll { it is Forecast }
        notifyItemRangeChanged(INDEX_FORECAST, weatherData.size)
        weatherData.addAll(INDEX_FORECAST, forecasst)
        notifyItemRangeChanged(INDEX_FORECAST, weatherData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            INDEX_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemContainerCurrentLocationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            //hiện thị dữ liệu trả về
            INDEX_FORECAST -> ForecastViewHolder(
                ItemContainerForecastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> CurrentWeatherViweHolder(
                ItemContainerCurrentWeatherBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is CurrentLocationViewHolder -> holder.bind(weatherData[position] as CurrentLocation)
            is CurrentWeatherViweHolder -> holder.bind(weatherData[position] as CurrentWeather)
            is ForecastViewHolder -> holder.bind(weatherData[position] as Forecast) //gọi phương thức bind và truyền vào dữ liệu  được ép kiểu thành Forecast.
        }

    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(weatherData[position]){
            is CurrentLocation -> INDEX_CURRENT_LOCATION
            is CurrentWeather -> INDEX_CURRENT_WEATHER
            is Forecast -> INDEX_FORECAST //của tuiiii
        }
    }

    inner class CurrentLocationViewHolder(
        private val binding: ItemContainerCurrentLocationBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentLocation: CurrentLocation){
            with(binding){
                textCurrentDate.text = currentLocation.date
                textCurrentLocation.text = currentLocation.location
                imageCurrentLocation.setOnClickListener{onLocationClicked()}
                textCurrentLocation.setOnClickListener{onLocationClicked()}
            }
        }
    }

    inner class CurrentWeatherViweHolder(
        private val binding: ItemContainerCurrentWeatherBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(currentWeather: CurrentWeather){
            with(binding){
                imageIcon.load("https:${currentWeather.icon}") {crossfade(true)}
                textTemperature.text=String.format("%s\u00B0C", currentWeather.temperature)
                textWind.text = String.format("%s km/h", currentWeather.wind)
                textHumidity.text = String.format("%s%%", currentWeather.humidity)
                textChanceOfRain.text = String.format("%s%%", currentWeather.chanceOfRain)
            }
        }
    }
    //quản lý và hiển thị dữ liệu cho từng item của RecyclerView
    inner class ForecastViewHolder(
        private val binding: ItemContainerForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        //hàm bind: thiết lập các thuộc tính của giao diện thời tiết bao gồm: ngày giờ, nhiệt độ, cảm giác như ... độ, icon thời tiết phù hợp với nhiệt độ đó(có mưa, mây, hay mặt trời, mặt trăng,..)
        fun bind(forecast: Forecast){
            with(binding){
                textTime.text = forecast.time
                textTemperature.text = String.format("%s\u00B0C", forecast.temperature)
                textFeelsLikeTemperature.text= String.format("%s\u00B0C", forecast.feelslikeTemperature)
                imageIcon.load("https${forecast.icon}") {crossfade(true)}

            }
        }
    }
}