package com.reggya.registeroffline.di

import android.content.Context
import androidx.room.Room
import com.reggya.registeroffline.data.local.room.MemberDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MemberDatabase {
         return Room.databaseBuilder(
             context,
             MemberDatabase::class.java,
             "users_database"
         ).build()
    }
}