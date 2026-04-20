package com.reggya.registeroffline.domain.repositories

import com.reggya.registeroffline.domain.model.Auth
import com.reggya.registeroffline.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import com.reggya.registeroffline.domain.model.Result

interface UserRepository {
    fun login(email: String, password: String): Flow<Result<Auth>>
    fun getProfile(): Flow<Result<Profile>>
    suspend fun logout()
    fun isLoggedIn(): Flow<Result<Boolean>>
}