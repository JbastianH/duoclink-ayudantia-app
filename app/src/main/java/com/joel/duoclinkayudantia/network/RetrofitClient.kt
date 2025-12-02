package com.joel.duoclinkayudantia.network

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://duoclink.vercel.app/"

    private val authInterceptor = Interceptor { chain ->
        val user = FirebaseAuth.getInstance().currentUser
        val requestBuilder = chain.request().newBuilder()

        if (user != null) {
            try {
                // Obtener el token de forma síncrona (bloqueante) porque estamos en un hilo de fondo de OkHttp
                val tokenResult = Tasks.await(user.getIdToken(true))
                val token = tokenResult.token
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: AyudantiaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AyudantiaApi::class.java)
    }
}
