package com.reggya.registeroffline.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.reggya.registeroffline.data.remote.network.ApiService
import com.reggya.registeroffline.data.local.preferences.AuthPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.reggya.registeroffline.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        authPreferences: AuthPreferences
    ): AuthInterceptor {
        return AuthInterceptor(authPreferences)
    }

    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .redactHeaders("Authorization")
            .alwaysReadResponseBody(true)
            .build()
        
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(client: OkHttpClient): ApiService =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
}