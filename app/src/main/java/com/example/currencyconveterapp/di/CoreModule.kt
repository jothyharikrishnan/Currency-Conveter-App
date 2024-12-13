package com.example.currencyconveterapp.di

import com.example.currencyconveterapp.data.remote.ExchangeApiService
import com.example.currencyconveterapp.data.repository.ExchangeRepositoryImpl
import com.example.currencyconveterapp.domain.usecase.ConvertUserCase
import com.example.currencyconveterapp.domain.repository.ExchangeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule{

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/v6/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExchangeApiService(retrofit: Retrofit): ExchangeApiService {
        return retrofit.create(ExchangeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRepository(exchangeApiService: ExchangeApiService): ExchangeRepository {
        return ExchangeRepositoryImpl(exchangeApiService=exchangeApiService)
    }

    @Provides
    @Singleton
    fun provideConvertUserCase(exchangeRepository: ExchangeRepository): ConvertUserCase {
        return ConvertUserCase(exchangeRepository = exchangeRepository)
    }

}