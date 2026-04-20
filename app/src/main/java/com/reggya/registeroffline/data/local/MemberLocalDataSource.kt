package com.reggya.registeroffline.data.local

import com.reggya.registeroffline.data.local.entities.MemberEntity
import com.reggya.registeroffline.data.local.room.MemberDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberLocalDataSource @Inject constructor(private val memberDatabase: MemberDatabase) {
    private val memberDao = memberDatabase.memberDao()
    fun getMembers(): Flow<List<MemberEntity>> = memberDao.getAllDraft()
    fun getUserById(id: Long): Flow<MemberEntity?> = memberDao.getById(id)
    suspend fun insertMember(memberEntity: MemberEntity) = memberDao.insert(memberEntity)
    suspend fun deleteMember(id: Long) = memberDao.deleteById(id)
    suspend fun deleteAllMember() = memberDao.deleteAll()
    suspend fun updateMember(memberEntity: MemberEntity) = memberDao.update(memberEntity)

}