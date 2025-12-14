package com.joel.duoclinkayudantia.model

import com.google.gson.annotations.SerializedName

data class Apunte(
    val id: String = "",
    val type: String = "text",
    @SerializedName("title")
    val titulo: String = "",
    @SerializedName("body")
    val descripcion: String? = null,
    val link: String? = null,
    val tags: List<String>? = emptyList(),
    @SerializedName("createdAt")
    val fecha: String? = null,
    val userId: String? = null
)


