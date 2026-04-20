package com.reggya.registeroffline.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reggya.registeroffline.data.local.entities.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: MemberEntity): Long

    @Update
    suspend fun update(member: MemberEntity)

    @Query("DELETE FROM members WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM members")
    fun getAllDraft(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getById(id: Long): Flow<MemberEntity?>
    @Query("DELETE FROM members")
    suspend fun deleteAll()

}