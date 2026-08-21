package com.marwaeltayeb.currencyexchange.core.di

import android.content.Context
import com.marwaeltayeb.currencyexchange.data.remote.RatesService
import com.marwaeltayeb.currencyexchange.core.utils.Constant.Companion.BASE_URL
import com.marwaeltayeb.currencyexchange.core.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val TAG = "NetworkModule"

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            // Offline Interceptor
            .addInterceptor { chain ->
                var request = chain.request()
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=${60 * 60 * 24 * 7}") // Cache for 7 days
                        .build()
                }
                chain.proceed(request)
            }
            // Network Interceptor
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=600") // Cache for 10 minute
                    .removeHeader("Pragma")
                    .build()
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