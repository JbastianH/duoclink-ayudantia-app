package com.joel.duoclinkayudantia.repository


import com.joel.duoclinkayudantia.model.Apunte

interface ApuntesRepository {
    suspend fun obtenerApuntes(): List<Apunte>
}