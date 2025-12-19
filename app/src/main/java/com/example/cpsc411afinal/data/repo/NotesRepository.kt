package com.example.cpsc411afinal.data.repo

import com.example.cpsc411afinal.data.local.FolderDao
import com.example.cpsc411afinal.data.local.NoteDao
import com.example.cpsc411afinal.data.local.FolderEntity
import com.example.cpsc411afinal.data.local.NoteEntity

class NotesRepository(
    private val folderDao: FolderDao,
    private val noteDao: NoteDao
) {
    fun folders() = folderDao.getAll()
    fun notes(folderId: Long) = noteDao.notesForFolder(folderId)

    suspend fun addFolder(name: String) =
        folderDao.insert(com.example.cpsc411afinal.data.local.FolderEntity(name = name))

    suspend fun deleteFolder(folder: com.example.cpsc411afinal.data.local.FolderEntity) =
        folderDao.delete(folder)

    suspend fun addNote(folderId: Long, title: String, content: String) {
        val now = System.currentTimeMillis()
        noteDao.insert(
            com.example.cpsc411afinal.data.local.NoteEntity(
                folderId = folderId,
                title = title,
                content = content,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun updateNote(note: com.example.cpsc411afinal.data.local.NoteEntity) {
        val now = System.currentTimeMillis()
        noteDao.update(note.copy(updatedAt = now))
    }

    suspend fun deleteNote(note: com.example.cpsc411afinal.data.local.NoteEntity) =
        noteDao.delete(note)

    suspend fun getNoteById(id: Long): NoteEntity? = noteDao.getById(id)

}


