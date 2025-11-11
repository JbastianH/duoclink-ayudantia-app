package com.joel.duoclinkayudantia.model.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

object UserPrefs {
    private val KEY_NOMBRE = stringPreferencesKey("nombre")
    private val KEY_APELLIDO = stringPreferencesKey("apellido")
    private val KEY_FOTO_URI = stringPreferencesKey("foto_uri")

    fun nombreFlow(context: Context) =
        context.dataStore.data.map { it[KEY_NOMBRE].orEmpty() }

    fun apellidoFlow(context: Context) =
        context.dataStore.data.map { it[KEY_APELLIDO].orEmpty() }

    fun fotoUriFlow(context: Context) =
        context.dataStore.data.map { it[KEY_FOTO_URI] }

    suspend fun save(context: Context, nombre: String, apellido: String, fotoUri: String?) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE] = nombre
            prefs[KEY_APELLIDO] = apellido
            if (fotoUri != null) {
                prefs[KEY_FOTO_URI] = fotoUri
            } else {
                prefs.remove(KEY_FOTO_URI)
            }
        }
    }
}