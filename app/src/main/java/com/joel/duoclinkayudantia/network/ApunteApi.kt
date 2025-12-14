package com.joel.duoclinkayudantia.network

import com.joel.duoclinkayudantia.model.Apunte
import com.joel.duoclinkayudantia.model.ApunteRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApunteApi {
    @GET("api/apuntes")
    suspend fun getApuntes(): List<Apunte>

    @GET("api/apuntes/{id}")
    suspend fun getApunte(@Path("id") id: String): Apunte

    @POST("api/apuntes")
    suspend fun crearApunte(@Body apunte: ApunteRequest): Apunte

    @PUT("api/apuntes/{id}")
    suspend fun actualizarApunte(@Path("id") id: String, @Body apunte: ApunteRequest): Map<String, String>

    @DELETE("api/apuntes/{id}")
    suspend fun eliminarApunte(@Path("id") id: String)
}
