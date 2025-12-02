package com.joel.duoclinkayudantia.network

import com.joel.duoclinkayudantia.model.Ayudantia
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AyudantiaApi {
    @GET("api/ayudantias")
    suspend fun getAyudantias(): List<Ayudantia>

    @POST("api/ayudantias")
    suspend fun crearAyudantia(@Body ayudantia: Ayudantia): Ayudantia

    @PUT("api/ayudantias/{id}")
    suspend fun actualizarAyudantia(@Path("id") id: String, @Body ayudantia: Ayudantia): Ayudantia

    @DELETE("api/ayudantias/{id}")
    suspend fun eliminarAyudantia(@Path("id") id: String)
}
