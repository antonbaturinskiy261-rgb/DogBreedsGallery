package com.example.dogbreedsgallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogbreedsgallery.data.repository.BreedRepository

class BreedListViewModelFactory(
    private val repo: BreedRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BreedListViewModel(repo) as T
    }
}