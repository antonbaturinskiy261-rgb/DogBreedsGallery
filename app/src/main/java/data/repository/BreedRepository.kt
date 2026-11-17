package com.example.dogbreedsgallery.data.repository

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.BreedDto
import com.example.dogbreedsgallery.data.remote.DogApi
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val api: DogApi,
    private val dao: BreedDao
) {
    // Получаем данные из БД (с поиском или без)
    fun observeBreeds(query: String = ""): Flow<List<BreedEntity>> =
        if (query.isBlank()) dao.observeAll() else dao.search(query)

    // Получаем избранное
    fun observeFavorites(): Flow<List<BreedEntity>> = dao.observeFavorites()

    // Обновляем данные из сети и сохраняем в БД
    suspend fun refresh() {
        val remote = api.getBreeds()
        dao.insertAll(remote.map { it.toEntity() })
    }

    // Переключаем избранное
    suspend fun toggleFavorite(id: Int, current: Boolean) {
        dao.setFavorite(id, !current)
    }

    // Маппинг DTO -> Entity
    private fun BreedDto.toEntity(): BreedEntity = BreedEntity(
        id = id,
        name = name,
        temperament = temperament ?: "",
        origin = origin,
        lifeSpan = lifeSpan ?: "",
        description = bredFor,
        imageUrl = image?.url ?: "",
        isFavorite = false
    )
}