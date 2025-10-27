package com.example.mybank.di

import com.example.mybank.data.network.AccountDetailsApi
import com.example.mybank.data.network.AccountsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun httpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://68e7e57a10e3f82fbf4139d7.mockapi.io/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAccountsApi(retrofit: Retrofit): AccountsApi = retrofit.create(AccountsApi::class.java)

    @Provides
    fun provideAccountDetailsApi(retrofit: Retrofit): AccountDetailsApi =
        retrofit.create(AccountDetailsApi::class.java)
}