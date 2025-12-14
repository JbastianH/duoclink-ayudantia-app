package com.joel.duoclinkayudantia.repository

import com.joel.duoclinkayudantia.model.Apunte

interface ApuntesRepository {
    suspend fun listarApuntes(token: String): List<Apunte>
    suspend fun crearApunte(token: String, apunte: Apunte)
    suspend fun eliminarApunte(token: String, id: Long)
}
