package com.joel.duoclinkayudantia.repository

import com.google.firebase.auth.FirebaseAuth
import com.joel.duoclinkayudantia.model.Apunte
import com.joel.duoclinkayudantia.model.ApunteRequest
import com.joel.duoclinkayudantia.network.RetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApuntesRepository {
    private val auth = FirebaseAuth.getInstance()
    private val api = RetrofitClient.apunteApi

    fun getApuntes(): Flow<List<Apunte>> = flow {
        try {
            val items = api.getApuntes()
            emit(items)
        } catch (e: Exception) {
            android.util.Log.e("ApuntesRepository", "Error API", e)
            throw e
        }
    }

    suspend fun getApunte(id: String): Apunte {
        return api.getApunte(id)
    }

    suspend fun crearApunte(apunte: ApunteRequest): String {
        val response = api.crearApunte(apunte)
        return response.id
    }

    suspend fun actualizarApunte(id: String, apunte: ApunteRequest) {
        api.actualizarApunte(id, apunte)
    }

    suspend fun eliminarApunte(id: String) {
        api.eliminarApunte(id)
    }
}
