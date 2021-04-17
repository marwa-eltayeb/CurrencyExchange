package com.marwaeltayeb.currencyexchange.ui.conversion.di

import androidx.lifecycle.ViewModel
import com.marwaeltayeb.currencyexchange.di.ViewModelKey
import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ConvertModule {

    @Binds
    @IntoMap
    @ViewModelKey(ConvertViewModel::class)
    abstract fun bindViewModel(viewmodel: ConvertViewModel): ViewModel
}