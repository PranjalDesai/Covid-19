package com.pranjaldesai.coronavirustracker.extension

import timber.log.Timber

fun Any.callingMethod(): String {
    return Throwable().stackTrace[2].methodName
}

fun Any.logTag(): String {
    return this::class.java.simpleName
}

fun Any.log() {
    Timber.tag(this.logTag()).i(this.callingMethod())
}