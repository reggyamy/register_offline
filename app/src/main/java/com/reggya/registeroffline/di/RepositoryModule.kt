package com.reggya.registeroffline.di

import android.content.Context
import com.reggya.registeroffline.data.MemberRepositoryImpl
import com.reggya.registeroffline.data.UserRepositoryImpl
import com.reggya.registeroffline.data.local.MemberLocalDataSource
import com.reggya.registeroffline.data.local.datastore.UserDataStore
import com.reggya.registeroffline.data.local.preferences.AuthPreferences
import com.reggya.registeroffline.data.remote.network.ApiService
import com.reggya.registeroffline.domain.repositories.MemberRepository
import com.reggya.registeroffline.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
	
	@Provides
	@Singleton
	fun provideUserRepository(
		apiService: ApiService,
		userDataStore: UserDataStore,
		authPreferences: AuthPreferences,
		memberLocalDataSource: MemberLocalDataSource
	): UserRepository {
		return UserRepositoryImpl(apiService, userDataStore, authPreferences, memberLocalDataSource)
	}

	@Provides
	@Singleton
	fun provideMemberRepository(
		apiService: ApiService,
		memberLocalDataSource: MemberLocalDataSource,
		@ApplicationContext context: Context
	): MemberRepository {
		return MemberRepositoryImpl(apiService, memberLocalDataSource, context)
	}
}