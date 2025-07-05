package com.marwaeltayeb.currencyexchange.ui.conversion.di

import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertActivity
import dagger.Subcomponent

@Subcomponent(modules = [ConvertModule::class])
interface ConvertComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ConvertComponent
    }

    fun inject(activity: ConvertActivity)
}