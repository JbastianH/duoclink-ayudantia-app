package com.joel.duoclinkayudantia.model

data class ApunteRequest(
    val titulo: String,
    val tipo: String,
    val cuerpo: String? = null,
    val url: String? = null,
    val tags: List<String> = emptyList()
)
