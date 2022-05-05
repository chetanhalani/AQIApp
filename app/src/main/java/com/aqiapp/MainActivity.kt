package com.aqiapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
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


class MainActivity : AppCompatActivity() {

    private val viewModel: AQIListViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.connectionLiveStatus.observe(this) {
            binding.tvConnectionStatus.text = it
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.layoutContainer.id, AQIListFragment(), "AQIListFragment").commit()
        viewModel.connectSocket(this)
    }
}