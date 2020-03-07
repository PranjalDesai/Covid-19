package com.pranjaldesai.coronavirustracker.di

import android.content.Context
import android.net.ConnectivityManager
import com.pranjaldesai.coronavirustracker.helper.ConnectivityMonitor
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val networkingModule = module(override = true) {
    single { androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single { ConnectivityMonitor(connectivityManager = get()) }
    single { assembleAuthorizationHeaderRequestInterceptor() }
    single { establishLogging() }
}

fun assembleAuthorizationHeaderRequestInterceptor(): Interceptor {
    return Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()

        return@Interceptor chain.proceed(requestBuilder.build())
    }
}

fun establishLogging(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }