package com.aqiapp.viewmodel

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aqiapp.R
import com.aqiapp.extension.diffInSecondsWithCurrentTime
import com.aqiapp.extension.getColorFromResources
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AQIListViewModel : ViewModel() {
    private val localCityAQIList = ArrayList<CityWithAQIList>()
    private val mutableCitiesWithAQIList = MutableLiveData<List<CityWithAQIList>>()
    val cityWithAQIList : LiveData<List<CityWithAQIList>> get() = mutableCitiesWithAQIList

    @Keep
    class CityWithAQIList(private val cityName: String?, private val aqiList: ArrayList<Double>?, private val date: Date) :
        Parcelable {

        private val FEW_SECONDS_AGO = "A few seconds ago"
        private val SUFFIX_MINUTE_AGO = " minute ago"
        private val TIME_FORMAT = "hh:mm a"

        private val FEW_SECONDS_AGO_FOR_SECODS = 60
        private val SUFFIX_MINUTE_AGO_FOR_SECONDS = 600

        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createDoubleArray()?.toMutableList() as ArrayList<Double>?,
            parcel.readSerializable() as Date
        ) {

        }

        fun getCityName() : String {
            return cityName ?: ""
        }

        fun addAQIToList(aqi: Double) {
            aqiList?.add(aqi)
        }

        fun getAQIList(): ArrayList<Double>? {
            return aqiList
        }

        fun getLatestAQI() : String {
            return getLatestAQIInDouble().toString()
        }

        fun getLatestAQIInDouble() : Double {
            aqiList?.let {
                return it.last()
            }
            return 0.0
        }

        fun getTextColorForAQI(mContext: Context) : Int {
            val aqi = getLatestAQIInDouble()
            return when {
                aqi < 50 -> {
                    mContext.getColorFromResources(R.color.healthy)
                }
                aqi < 100 -> {
                    mContext.getColorFromResources(R.color.light_healthy)
                }
                aqi < 200 -> {
                    mContext.getColorFromResources(R.color.normal)
                }
                aqi < 300 -> {
                    mContext.getColorFromResources(R.color.cautious)
                }
                aqi < 400 -> {
                    mContext.getColorFromResources(R.color.severe)
                }
                else -> {
                    mContext.getColorFromResources(R.color.alert)
                }
            }
        }

        fun getLastUpdated() : String {
            val diffInSec: Long = date.diffInSecondsWithCurrentTime()
            return when {
                diffInSec < FEW_SECONDS_AGO_FOR_SECODS -> {
                    FEW_SECONDS_AGO
                }
                diffInSec < SUFFIX_MINUTE_AGO_FOR_SECONDS -> {
                    ((diffInSec/60).toString() + SUFFIX_MINUTE_AGO)
                }
                else -> {
                    SimpleDateFormat(TIME_FORMAT, Locale.US).format(date)
                }
            }
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.let {
                it.writeString(cityName)
                it.writeDoubleArray(aqiList?.toDoubleArray())
                it.writeSerializable(date)
            }
        }

        companion object CREATOR : Parcelable.Creator<CityWithAQIList> {
            override fun createFromParcel(parcel: Parcel): CityWithAQIList {
                return CityWithAQIList(parcel)
            }

            override fun newArray(size: Int): Array<CityWithAQIList?> {
                return arrayOfNulls(size)
            }
        }

    }

    fun processCityWithAQI(list: List<AQICityModel>) {
        for (i in list.indices) {
            val existingObject = getCityFromList(list[i])
            if (existingObject != null) {
                addAQIWithCity(existingObject, list[i])
            } else {
                addCityWithAQI(list[i])
            }
        }
        mutableCitiesWithAQIList.postValue(localCityAQIList)
    }

    private fun addCityWithAQI(aqiCityModel: AQICityModel) {
        localCityAQIList.add(CityWithAQIList(cityName = aqiCityModel.cityName, aqiList = arrayListOf(aqiCityModel.aqiValue), date = Date()))
    }

    private fun getCityFromList(aqiCityModel: AQICityModel) : CityWithAQIList? {
        for(i in 0 until localCityAQIList.size) {
            if(localCityAQIList[i].getCityName() == aqiCityModel.cityName) {
                return localCityAQIList[i]
            }
        }
        return null
    }

    private fun addAQIWithCity(cityWithAQIList: CityWithAQIList, aqiCityModel: AQICityModel) {
        cityWithAQIList.addAQIToList(aqiCityModel.aqiValue)
    }

}
