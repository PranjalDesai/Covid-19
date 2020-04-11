package com.pranjaldesai.coronavirustracker


import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.pranjaldesai.coronavirustracker.di.*
import com.pranjaldesai.coronavirustracker.extension.logTag
import com.pranjaldesai.coronavirustracker.lifecycle.ApplicationState
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class CoreApplication : Application(), LifecycleObserver {

    val crashlyticsKit: Crashlytics by inject()
    private val useMultiDex: Boolean = true
    private var currentState: ApplicationState = ApplicationState.UNKNOWN
        private set(value) {
            field = value
            onApplicationStateChanged(value)
        }


    @CallSuper
    override fun onCreate() {
        super.onCreate()
        injectDependencies()
        plantTimber()
        observeLifecycle()
        initializeCrashlytics()
        currentState = ApplicationState.CREATED
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    @CallSuper
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (useMultiDex) {
            MultiDex.install(this)
        }
    }

    private fun onEnterForeground() {
        /* Stub! */
    }

    private fun onEnterBackground() {
        /* Stub! */
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun enteringForeground() {
        currentState = ApplicationState.MOVING_TO_FOREGROUND
        onEnterForeground()
        currentState = ApplicationState.IN_FOREGROUND
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun enteringBackground() {
        currentState = ApplicationState.MOVING_TO_BACKGROUND
        onEnterBackground()
        currentState = ApplicationState.IN_BACKGROUND
    }

    @CallSuper
    open fun onApplicationStateChanged(state: ApplicationState) {
        logApplicationStateChanged(state)
    }

    private fun observeLifecycle() = ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    private fun logApplicationStateChanged(state: ApplicationState) {
        Timber.tag(logTag()).d(state.toString())
    }

    private fun plantTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun injectDependencies() {
        startKoin(this)
    }

    private fun initializeCrashlytics() {
        Fabric.with(this, crashlyticsKit)
    }

    companion object DI {

        @JvmStatic
        fun startKoin(context: Context) {
            startKoin {
                androidContext(context)
                androidFileProperties()
                androidLogger(Level.DEBUG)
                modules(moduleList)
            }
        }

        @JvmStatic
        val moduleList = listOf(
            gsonModule,
            appModule,
            crashlyticsModule,
            networkingModule,
            picassoModule,
            dividerItemDecorationModule,
            snackbarModule
        )
    }
}