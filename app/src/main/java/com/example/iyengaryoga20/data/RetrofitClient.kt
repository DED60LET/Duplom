package com.example.iyengaryoga20.data

import com.example.iyengaryoga20.model.MobiClassElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MobiFitnessApiService {
    // ИЗМЕНЕНИЕ: Используем общий публичный API (без "personal-widget" и без "code")
    @GET("api/v6/schedule.json")
    suspend fun getSchedule(
        @Query("club_id") clubId: String,
        @Query("start") startDate: String, // YYYY-MM-DD
        @Query("end") endDate: String      // YYYY-MM-DD
    ): List<MobiClassElement>
}

object RetrofitClient {
    private const val BASE_URL = "https://mobifitness.ru/"

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    // Оставляем только User-Agent, остальные заголовки убираем, чтобы не смущать сервер
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val api: MobiFitnessApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MobiFitnessApiService::class.java)
    }
}