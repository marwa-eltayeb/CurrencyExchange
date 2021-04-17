package com.marwaeltayeb.currencyexchange.di

import android.content.Context
import com.marwaeltayeb.currencyexchange.ui.conversion.di.ConvertComponent
import com.marwaeltayeb.currencyexchange.ui.main.di.RatesComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
// Definition of a Dagger component that adds info from the different modules to the graph
@Component(
    modules = [
        NetworkModule::class,
        AppSubcomponents::class,
        ViewModelBuilderModule::class
    ]
)

interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    // Types that can be retrieved from the graph
    fun ratesComponent(): RatesComponent.Factory
    fun convertComponent(): ConvertComponent.Factory
}








//https://developer.android.com/training/dependency-injection/dagger-android?fbclid=IwAR2J2iTIdRjZaJ1xFZJ6OAA18v_CwSDtmCHD_QsC6t37Ae5CXn8_wk8EmjU#kotlin