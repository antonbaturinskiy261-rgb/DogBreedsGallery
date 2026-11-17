package com.example.dogbreedsgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repo: BreedRepository
) : ViewModel() {

    val favorites: StateFlow<List<BreedEntity>> = repo.observeFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repo.toggleFavorite(breed.id, breed.isFavorite)
        }
    }
}

class FavoritesViewModelFactory(
    private val repo: BreedRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FavoritesViewModel(repo) as T
    }
}