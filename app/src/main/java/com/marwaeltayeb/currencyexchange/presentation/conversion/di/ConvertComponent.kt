package com.marwaeltayeb.currencyexchange.presentation.conversion.di

import com.marwaeltayeb.currencyexchange.presentation.conversion.ConvertActivity
import dagger.Subcomponent

@Subcomponent(modules = [ConvertModule::class])
interface ConvertComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ConvertComponent
    }

    fun inject(activity: ConvertActivity)
}