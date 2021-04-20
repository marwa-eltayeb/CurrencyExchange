package com.marwaeltayeb.currencyexchange.di

import com.marwaeltayeb.currencyexchange.data.remote.RatesService
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRatesService(retrofit: Retrofit): RatesService{
        return retrofit.create(RatesService::class.java)
    }
}