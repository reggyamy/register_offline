package com.reggya.registeroffline.domain.repositories

import com.reggya.registeroffline.domain.model.Result
import com.reggya.registeroffline.domain.model.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
	suspend fun registerMember(member: Member)
	fun getMemberList(): Flow<Result<List<Member>>>
	fun uploadMember(member: Member): Flow<Result<Member>>

	fun getDraftMemberList(): Flow<List<Member>>
}