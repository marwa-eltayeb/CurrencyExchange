package com.marwaeltayeb.currencyexchange.core

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.marwaeltayeb.currencyexchange.BuildConfig
import com.marwaeltayeb.currencyexchange.core.di.AppComponent
import com.marwaeltayeb.currencyexchange.data.file.CurrencyData
import com.marwaeltayeb.currencyexchange.core.di.DaggerAppComponent
import timber.log.Timber

open class MianApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        CurrencyData.initialize(applicationContext)

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        DaggerAppComponent.factory().create(applicationContext)
    }
}