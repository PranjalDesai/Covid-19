package com.pranjaldesai.coronavirustracker.di

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

const val COVID_19_CACHE_DIRECTORY = "covid_19_image_cache"

val picassoModule = module {
    single {
        picassoBuilder(
            context = get(),
            requestInterceptor = get(),
            loggingInterceptor = get()
        )
    }
}

fun assembleHttpClient(
    requestInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor,
    cache: Cache
): OkHttp3Downloader {
    val client = OkHttpClient.Builder()
        .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(requestInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    return OkHttp3Downloader(client)
}

fun buildCache(context: Context): Cache {
    val cacheDirectory = File(context.cacheDir, COVID_19_CACHE_DIRECTORY).apply { mkdirs() }
    return Cache(cacheDirectory, Long.MAX_VALUE)
}

fun picassoBuilder(
    context: Context,
    requestInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor
): Picasso {
    val cache = buildCache(context)
    val downloadClient = assembleHttpClient(requestInterceptor, loggingInterceptor, cache)
    return Picasso.Builder(context).downloader(downloadClient).build()
}