package com.example.cpsc411afinal.ui.theme

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val FOLDERS = "folders"

    // args
    const val NOTES = "notes/{folderId}/{folderName}"
    fun notes(folderId: Long, folderName: String) = "notes/$folderId/$folderName"

    const val EDIT_NOTE = "editNote/{folderId}/{folderName}?noteId={noteId}"
    fun editNote(folderId: Long, folderName: String, noteId: Long? = null) =
        "editNote/$folderId/$folderName?noteId=${noteId ?: -1}"
}


