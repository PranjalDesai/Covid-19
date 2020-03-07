package com.pranjaldesai.coronavirustracker.data

import com.pranjaldesai.coronavirustracker.di.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface ICoreProvider : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = DispatcherProvider.provideIOContext()

}