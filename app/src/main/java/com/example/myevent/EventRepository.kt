package com.example.myevent

import androidx.lifecycle.LiveData

class EventRepository(private val favoriteEventDao: FavoriteEventDao) {

    val favoriteEvents: LiveData<List<FavoriteEventEntity>> = favoriteEventDao.getAllFavorites()

    suspend fun addFavorite(event: FavoriteEventEntity) {
        favoriteEventDao.addFavorite(event)
    }

    suspend fun removeFavorite(event: FavoriteEventEntity) {
        favoriteEventDao.removeFavorite(event)
    }
}
