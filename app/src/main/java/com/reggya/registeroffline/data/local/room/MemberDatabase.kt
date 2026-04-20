package com.reggya.registeroffline.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reggya.registeroffline.data.local.entities.MemberEntity

@Database(entities = [MemberEntity::class], version = 1, exportSchema = false)
abstract class MemberDatabase : RoomDatabase() {
	
	abstract fun memberDao(): MemberDao
}