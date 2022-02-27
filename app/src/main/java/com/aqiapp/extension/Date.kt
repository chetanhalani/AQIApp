package com.aqiapp.extension

import java.util.*
import java.util.concurrent.TimeUnit

fun Date.diffInSecondsWithCurrentTime() : Long {
    val diffInMs: Long = Date().time - this.time
    return TimeUnit.MILLISECONDS.toSeconds(diffInMs)
}