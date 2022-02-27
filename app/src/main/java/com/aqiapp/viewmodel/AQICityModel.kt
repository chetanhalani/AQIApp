package com.aqiapp.viewmodel

import com.google.gson.annotations.SerializedName

data class AQICityModel(@SerializedName("city") val cityName: String,
                        @SerializedName("aqi") val aqiValue: Double,
)