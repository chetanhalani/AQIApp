package com.aqiapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aqiapp.databinding.ActivityMainBinding
import com.aqiapp.extension.diffInSecondsWithCurrentTime
import com.aqiapp.journey.AQIListFragment
import com.aqiapp.viewmodel.AQICityModel
import com.aqiapp.viewmodel.AQIListViewModel
import com.aqiapp.websocket.AQIUpdate
import com.aqiapp.websocket.AQIWebSocket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class MainActivity : AppCompatActivity(), AQIUpdate {

    private val viewModel: AQIListViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocket: AQIWebSocket

    private val gson = Gson()
    private val gsoAQIListCityModelType = object : TypeToken<List<AQICityModel>>() {}.type
    private var lastUpdateDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        webSocket = AQIWebSocket(this,this)
        supportFragmentManager.beginTransaction()
            .replace(binding.layoutContainer.id, AQIListFragment(), "AQIListFragment").commit()
    }

    override fun onResume() {
        super.onResume()
        webSocket.connect()
    }

    override fun onPause() {
        super.onPause()
        webSocket.disconnect()
    }

    override fun connectionUpdate(connectionStatus: String) {
        binding.tvConnectionStatus.text = connectionStatus
    }

    override fun dataUpdate(message: String) {
//        if (isUpdateAfter30Sec()) {
            val aqiList = gson.fromJson<List<AQICityModel>>(message, gsoAQIListCityModelType)
            viewModel.processCityWithAQI(aqiList)
            lastUpdateDate = Date()
//        }
    }

    private fun isUpdateAfter30Sec(): Boolean {
        lastUpdateDate?.let {
            if (it.diffInSecondsWithCurrentTime() < 30) {
                return false
            }
        }
        return true
    }
}