package com.marwaeltayeb.currencyexchange.di

import com.marwaeltayeb.currencyexchange.ui.conversion.di.ConvertComponent
import com.marwaeltayeb.currencyexchange.ui.main.di.RatesComponent
import dagger.Module


// This module tells a Component which are its subcomponents
@Module(
    subcomponents = [
        RatesComponent::class,
        ConvertComponent::class,
    ]
)
class AppSubcomponents