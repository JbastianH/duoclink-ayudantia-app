package com.joel.duoclinkayudantia.data

import androidx.room.*
import com.joel.duoclinkayudantia.model.Ayudantia
import kotlinx.coroutines.flow.Flow

@Dao
interface AyudantiaDao {
    @Query("SELECT * FROM ayudantias ORDER BY id DESC")
    fun getAll(): Flow<List<Ayudantia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ayudantia: Ayudantia): Long

    @Update
    suspend fun update(ayudantia: Ayudantia)

    @Delete
    suspend fun delete(ayudantia: Ayudantia)

    @Query("SELECT * FROM ayudantias WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Ayudantia?
}