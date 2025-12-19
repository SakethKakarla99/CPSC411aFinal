package com.example.cpsc411afinal

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cpsc411afinal.data.local.AppDatabase
import com.example.cpsc411afinal.data.repo.AuthRepository
import com.example.cpsc411afinal.data.repo.NotesRepository
import com.example.cpsc411afinal.data.repo.UserPrefsRepository

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notes ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE notes ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
    }
}

class AppContainer(context: Context) {
    val authRepository = AuthRepository()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "notes.db"
    )
        .addMigrations(MIGRATION_1_2)
        .build()

    val notesRepository = NotesRepository(db.folderDao(), db.noteDao())

    val userPrefsRepository = UserPrefsRepository(context)
}

