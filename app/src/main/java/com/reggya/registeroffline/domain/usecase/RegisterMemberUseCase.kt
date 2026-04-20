package com.reggya.registeroffline.domain.usecase

import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.domain.repositories.MemberRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(member: Member) {
        memberRepository.registerMember(member)
    }
}