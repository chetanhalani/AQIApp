package com.aqiapp.websocket

import android.content.Context
import com.aqiapp.R
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit


class AQIWebSocket(private val mContext: Context, private val dataHandler: AQIUpdate) : WebSocketListener() {

    private val mClient: OkHttpClient = OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS).build()
    private lateinit var webSocket: WebSocket
    private val CONNECT_URL = "ws://city-ws.herokuapp.com/"

    private val USER_CLOSE_CODE = 1001

    fun connect() {
        val request: Request = Request.Builder().url(CONNECT_URL).build()
        webSocket = mClient.newWebSocket(request, this)
//        mClient.dispatcher().executorService().shutdown()
    }

    fun disconnect() {
        webSocket.close(USER_CLOSE_CODE, mContext.getString(R.string.user_requested))
    }

    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        super.onOpen(webSocket, response)
        dataHandler.connectionUpdate(mContext.getString(R.string.connected))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        dataHandler.dataUpdate(text)
    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        super.onClosed(webSocket, code, reason)
        dataHandler.connectionUpdate(mContext.getString(R.string.disconnected))
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        super.onClosing(webSocket, code, reason)
        dataHandler.connectionUpdate(mContext.getString(R.string.disconnecting))
    }
}