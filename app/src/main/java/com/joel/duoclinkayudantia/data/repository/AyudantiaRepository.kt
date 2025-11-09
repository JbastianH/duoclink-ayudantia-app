package com.joel.duoclinkayudantia.data.repository

import com.joel.duoclinkayudantia.data.AyudantiaDao
import com.joel.duoclinkayudantia.model.Ayudantia
import kotlinx.coroutines.flow.Flow

class AyudantiaRepository(private val dao: AyudantiaDao) {
    val ayudantias: Flow<List<Ayudantia>> = dao.getAll()

    suspend fun upsert(ayudantia: Ayudantia) {
        if (ayudantia.id == 0) dao.insert(ayudantia) else dao.update(ayudantia)
    }

    suspend fun delete(ayudantia: Ayudantia) = dao.delete(ayudantia)

    suspend fun get(id: Int) = dao.getById(id)
}