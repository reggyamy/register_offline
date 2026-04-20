package com.reggya.registeroffline.domain.usecase

import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<Boolean>> {
        return userRepository.isLoggedIn()
    }
}