package com.example.dogbreedsgallery.data.remote

import retrofit2.http.GET

interface DogApi {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>
}