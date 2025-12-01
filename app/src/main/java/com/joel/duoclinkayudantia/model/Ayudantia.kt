package com.joel.duoclinkayudantia.model

import com.google.firebase.Timestamp

data class Ayudantia(
    val id: String = "",
    val materia: String = "",
    val cupo: Int = 0,
    val inscritos: Int = 0,
    val horario: String = "",
    val dia: String = "",
    val lugar: String = "",
    val autor: Autor = Autor(),
    val creado: Timestamp? = null
)

data class Autor(
    val uid: String = "",
    val nombre: String = ""
)