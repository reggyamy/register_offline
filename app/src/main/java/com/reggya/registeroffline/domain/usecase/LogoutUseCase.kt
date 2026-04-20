package com.reggya.registeroffline.domain.usecase

import com.reggya.registeroffline.domain.model.Auth
import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.logout()
    }
}