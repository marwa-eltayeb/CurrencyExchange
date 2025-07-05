package com.marwaeltayeb.currencyexchange.ui.main.di

import com.marwaeltayeb.currencyexchange.ui.main.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [RatesModule::class])
interface RatesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RatesComponent
    }

    fun inject(activity: MainActivity)
}