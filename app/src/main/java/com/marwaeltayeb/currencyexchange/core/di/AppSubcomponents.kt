package com.marwaeltayeb.currencyexchange.core.di

import com.marwaeltayeb.currencyexchange.presentation.conversion.di.ConvertComponent
import com.marwaeltayeb.currencyexchange.presentation.main.di.RatesComponent
import dagger.Module


// This module tells a Component which are its subcomponents
@Module(
    subcomponents = [
        RatesComponent::class,
        ConvertComponent::class,
    ]
)
class AppSubcomponents