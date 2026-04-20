package com.reggya.registeroffline.domain.usecase

import com.reggya.registeroffline.domain.model.Profile
import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<Profile>> {
        return userRepository.getProfile()
    }
}