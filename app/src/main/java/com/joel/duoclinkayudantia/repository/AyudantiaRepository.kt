package com.joel.duoclinkayudantia.repository

import com.google.firebase.auth.FirebaseAuth
import com.joel.duoclinkayudantia.model.Ayudantia
import com.joel.duoclinkayudantia.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AyudantiaRepository {
    private val auth = FirebaseAuth.getInstance()
    private val api = RetrofitClient.api

    fun getAyudantias(): Flow<List<Ayudantia>> = flow {
        android.util.Log.d("AyudantiaRepository", "Consultando ayudantías API. Usuario: ${auth.currentUser?.email}")
        try {
            val items = api.getAyudantias()
            emit(items)
        } catch (e: Exception) {
            android.util.Log.e("AyudantiaRepository", "Error API", e)
            throw e
        }
    }

    suspend fun crearAyudantia(ayudantia: Ayudantia): String {
        // El API se encarga de asignar el autor basado en el token
        val response = api.crearAyudantia(ayudantia)
        return response.id
    }

    suspend fun actualizarAyudantia(id: String, ayudantia: Ayudantia) {
        api.actualizarAyudantia(id, ayudantia)
    }

    suspend fun eliminarAyudantia(id: String) {
        api.eliminarAyudantia(id)
    }

    suspend fun unirse(ayudantiaId: String) {
        // TODO: El API actual no soporta la funcionalidad de 'unirse' para usuarios que no son el autor.
        // Se requiere un endpoint POST /api/ayudantias/{id}/join o similar.
        android.util.Log.w("AyudantiaRepository", "Funcionalidad 'unirse' no implementada en API REST")
        throw Exception("Funcionalidad no disponible en API")
    }
}