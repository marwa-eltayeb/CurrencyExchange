package com.marwaeltayeb.currencyexchange.presentation.main.di

import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.core.di.ViewModelKey
import com.marwaeltayeb.currencyexchange.presentation.main.RatesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RatesModule {

    @Binds
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    abstract fun bindViewModel(viewmodel: RatesViewModel): ViewModel

}