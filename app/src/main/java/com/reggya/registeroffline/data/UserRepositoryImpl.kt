package com.reggya.registeroffline.data

import com.reggya.registeroffline.data.local.MemberLocalDataSource
import com.reggya.registeroffline.data.utils.mapper.toDomain
import com.reggya.registeroffline.data.local.datastore.UserDataStore
import com.reggya.registeroffline.data.local.preferences.AuthPreferences
import com.reggya.registeroffline.data.remote.network.ApiService
import com.reggya.registeroffline.data.remote.request.LoginRequest
import com.reggya.registeroffline.domain.model.Auth
import com.reggya.registeroffline.domain.model.Profile
import com.reggya.registeroffline.domain.repositories.UserRepository
import com.reggya.registeroffline.domain.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDataStore: UserDataStore,
    private val authPreferences: AuthPreferences,
    private val memberLocalDataSource: MemberLocalDataSource
) : UserRepository {

    override fun login(
        email: String,
        password: String
    ): Flow<Result<Auth>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.login(LoginRequest(email = email, password = password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        authPreferences.saveToken(token = body.toDomain().token )
                        emit(Result.Success(body.toDomain()))
                    } else {
                        emit(Result.Error(message = "Response body kosong"))
                    }
                } else {
                    emit(Result.Error(message = "Login gagal: ${response.message()}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(message = e.message.toString()))
            }
        }
    }

    override fun getProfile(): Flow<Result<Profile>> {
        return flow {
            emit(Result.Loading)
            try {
                val cached = userDataStore.getUser().first()
                if (cached != null) {
                    emit(Result.Success(cached))
                    return@flow
                }

                val response = apiService.getProfile()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val domainUser = body.toDomain()
                        userDataStore.saveUser(domainUser)
                        emit(Result.Success(domainUser))
                    } else {
                        emit(Result.Error("Response body kosong"))
                    }
                } else {
                    emit(Result.Error("Gagal: ${response.code()}"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun logout() {
        authPreferences.clear()
        userDataStore.clear()
        memberLocalDataSource.deleteAllMember()
    }

    override fun isLoggedIn(): Flow<Result<Boolean>> {
        return flow {
            emit(Result.Loading)
            val isLogin = !authPreferences.getToken().isNullOrBlank()
            emit(Result.Success(isLogin))
        }.flowOn(Dispatchers.IO)
    }

}
