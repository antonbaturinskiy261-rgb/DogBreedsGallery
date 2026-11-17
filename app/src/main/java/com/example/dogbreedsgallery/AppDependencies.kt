package com.example.dogbreedsgallery

import android.content.Context
import com.example.dogbreedsgallery.data.local.AppDatabase
import com.example.dogbreedsgallery.data.remote.DogApi
import com.example.dogbreedsgallery.data.repository.BreedRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppDependencies {
    lateinit var repository: BreedRepository
        private set

    fun init(context: Context) {
        // Инициализация БД
        val db = AppDatabase.get(context)

        // Настройка Moshi
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        // Настройка OkHttp с логированием
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        // Настройка Retrofit
        val api = Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/")
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DogApi::class.java)

        // Создание Repository
        repository = BreedRepository(api, db.breedDao())
    }
}