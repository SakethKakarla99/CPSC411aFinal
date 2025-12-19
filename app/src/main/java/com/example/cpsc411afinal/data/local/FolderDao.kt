package com.example.cpsc411afinal.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders")
    fun getAll(): Flow<List<FolderEntity>>

    @Insert
    suspend fun insert(folder: FolderEntity)

    @Delete
    suspend fun delete(folder: FolderEntity)
}


