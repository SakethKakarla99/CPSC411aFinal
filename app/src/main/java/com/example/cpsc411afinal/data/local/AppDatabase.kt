package com.example.cpsc411afinal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FolderEntity::class, NoteEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun noteDao(): NoteDao
}

