package com.example.dogbreedsgallery.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object BreedListScreen : NavKey

@Serializable
data class BreedDetailScreen(val breedId: Int) : NavKey

@Serializable
data object FavoritesScreen : NavKey