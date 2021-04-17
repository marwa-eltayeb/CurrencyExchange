package com.marwaeltayeb.currencyexchange.ui.main.di

import com.marwaeltayeb.currencyexchange.ui.main.MainActivity
import dagger.Subcomponent

// Definition of a Dagger subcomponent
@Subcomponent(modules = [RatesModule::class])
interface RatesComponent {

    // Factory to create instances of UserComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): RatesComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: MainActivity)
}