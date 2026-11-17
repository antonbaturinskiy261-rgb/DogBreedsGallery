package com.example.dogbreedsgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.repository.BreedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BreedListViewModel(
    private val repo: BreedRepository
) : ViewModel() {

    // Поиск
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Список пород (реактивно обновляется при поиске)
    val breeds: StateFlow<List<BreedEntity>> = _query
        .flatMapLatest { q -> repo.observeBreeds(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Состояние обновления (Pull-to-refresh)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Ошибка
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onQueryChange(new: String) {
        _query.value = new
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null
            runCatching { repo.refresh() }
                .onFailure { _error.value = it.message }
            _isRefreshing.value = false
        }
    }

    fun toggleFavorite(breed: BreedEntity) {
        viewModelScope.launch {
            repo.toggleFavorite(breed.id, breed.isFavorite)
        }
    }
}