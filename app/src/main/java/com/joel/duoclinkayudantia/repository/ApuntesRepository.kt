package com.joel.duoclinkayudantia.repository

import com.joel.duoclinkayudantia.model.Apunte

// Interfaz define el contrato
interface ApuntesRepository {
    suspend fun obtenerApuntes(): List<Apunte>
}

// Implementación de la interfaz
class ApuntesRepositoryImpl : ApuntesRepository {
    override suspend fun obtenerApuntes(): List<Apunte> {
        // Aquí va la lógica real (actualmente retorna una lista vacía)
        return emptyList()
    }
}
