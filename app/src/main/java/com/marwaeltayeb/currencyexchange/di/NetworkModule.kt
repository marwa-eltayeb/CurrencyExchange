package com.marwaeltayeb.currencyexchange.di

import android.content.Context
import com.marwaeltayeb.currencyexchange.data.remote.RatesService
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.BASE_URL
import com.marwaeltayeb.currencyexchange.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

private const val TAG = "NetworkModule"

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                if (NetworkUtils.isNetworkAvailable(context)) {
                    request = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).removeHeader("Pragma").build()
                    Timber.tag(TAG).d("Response from Network")
                } else {
                    request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).removeHeader("Pragma").build()
                    Timber.tag(TAG).d("Response from Cache")
                }
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideRatesService(retrofit: Retrofit): RatesService {
        return retrofit.create(RatesService::class.java)
    }
}