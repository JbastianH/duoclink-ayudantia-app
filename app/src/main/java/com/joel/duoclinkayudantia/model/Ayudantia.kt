package com.joel.duoclinkayudantia.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ayudantias")
data class Ayudantia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val publicadoPor: String,
    val tema: String,
    val lugar: String,
    val hora: String,
    val dia: String,
    val cupos: Int,
    val duracion: Int
)