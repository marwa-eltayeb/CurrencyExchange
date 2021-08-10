package com.marwaeltayeb.currencyexchange

import android.app.Application
import com.marwaeltayeb.currencyexchange.di.AppComponent
import com.marwaeltayeb.currencyexchange.di.DaggerAppComponent
import timber.log.Timber

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }
}