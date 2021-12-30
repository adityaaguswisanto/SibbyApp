package com.sibby.sibby.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserStore(
    context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_store")

    private val mDataStore: DataStore<Preferences> = context.dataStore

    val authToken: Flow<String?> = mDataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[KEY_AUTH]
        }

    suspend fun saveToken(token: String) =
        mDataStore.edit {
            it[KEY_AUTH] = token
        }

    suspend fun clear() = mDataStore.edit {
        it.clear()
    }

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }

}