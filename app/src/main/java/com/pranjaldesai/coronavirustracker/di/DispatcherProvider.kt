package com.pranjaldesai.coronavirustracker.di

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object DispatcherProvider {
    fun provideUIContext(): CoroutineContext = Dispatchers.Main
    fun provideIOContext(): CoroutineContext = Dispatchers.IO
    fun provideDefaultContext(): CoroutineContext = Dispatchers.Default
}