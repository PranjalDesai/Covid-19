package com.pranjaldesai.coronavirustracker.extension

import com.crashlytics.android.Crashlytics
import org.koin.core.KoinComponent
import timber.log.Timber

object LogExt : KoinComponent {

    fun logd(tag: String, message: String, stack: Throwable? = null) {
        logd("$tag: $message", stack)
    }

    fun logd(message: String, stack: Throwable? = null) {
        if (stack == null) {
            Timber.d(message)
        } else {
            Timber.d(Throwable(message, stack))
        }
    }

    fun loge(throwable: Throwable) {
        logCrashlyticsException(throwable)
        Timber.e(throwable)
    }

    fun logCrashlyticsException(throwable: Throwable) {
        try {
            Crashlytics.logException(throwable)
        } catch (exception: Exception) {
            //This should only occur when running unit tests, since Crashlytics will not be initialized
            println(exception)
        }
    }

    fun loge(exception: Exception) {
        loge(exception as Throwable)
    }

    fun loge(message: String, stack: Throwable = Throwable()) {
        loge(Exception(message, stack))
    }

}