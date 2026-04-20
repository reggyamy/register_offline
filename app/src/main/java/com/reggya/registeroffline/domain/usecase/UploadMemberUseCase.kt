package com.reggya.registeroffline.domain.usecase

import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.domain.repositories.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadMemberUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(member: Member): Flow<Result<Member>> {
        return memberRepository.uploadMember(member)
    }
}