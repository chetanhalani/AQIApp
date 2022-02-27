package com.aqiapp.websocket

interface AQIUpdate {
    fun connectionUpdate(connectionStatus: String)
    fun dataUpdate(message: String)
}
