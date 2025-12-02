package com.joel.duoclinkayudantia.model

import com.google.gson.annotations.SerializedName

data class Ayudantia(
    val id: String = "",
    val materia: String = "",
    val cupo: Int = 0,
    val inscritos: Int = 0,
    val horario: String = "",
    val dia: String = "",
    val lugar: String = "",
    val autor: Autor = Autor(),
    @SerializedName("createdAt")
    val creado: String? = null
)

data class Autor(
    val uid: String = "",
    val nombre: String = ""
)