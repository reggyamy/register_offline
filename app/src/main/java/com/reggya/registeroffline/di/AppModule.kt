package com.reggya.registeroffline.di

import android.content.Context
import com.reggya.registeroffline.data.local.preferences.AuthPreferences
import com.reggya.registeroffline.data.local.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthPreferences(
        @ApplicationContext context: Context
    ): AuthPreferences {
        return AuthPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore {
        return UserDataStore(context)
    }

}