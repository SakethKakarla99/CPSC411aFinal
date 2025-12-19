package com.example.cpsc411afinal.data.repo

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPrefsRepository(private val context: Context) {

    private val KEY_NAME = stringPreferencesKey("display_name")

    val displayName: Flow<String> =
        context.dataStore.data.map { prefs -> prefs[KEY_NAME] ?: "" }

    suspend fun setDisplayName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = name
        }
    }
}


