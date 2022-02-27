package com.aqiapp.extension

import android.content.Context
import android.os.Build

fun Context.getColorFromResources(colorId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(colorId)
    } else {
        this.resources.getColor(colorId)
    }
}