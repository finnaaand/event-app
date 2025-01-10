package com.example.myevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {

    val favoriteEvents: LiveData<List<FavoriteEventEntity>> = eventRepository.favoriteEvents

    fun addFavorite(event: FavoriteEventEntity) {
        viewModelScope.launch {
            eventRepository.addFavorite(event)
        }
    }

    fun removeFavorite(event: FavoriteEventEntity) {
        viewModelScope.launch {
            eventRepository.removeFavorite(event)
        }
    }
}

