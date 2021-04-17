package com.marwaeltayeb.currencyexchange.ui.conversion.di

import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertActivity
import dagger.Subcomponent


// Definition of a Dagger subcomponent
@Subcomponent(modules = [ConvertModule::class])
interface ConvertComponent {

    // Factory to create instances of UserComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): ConvertComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: ConvertActivity)
}