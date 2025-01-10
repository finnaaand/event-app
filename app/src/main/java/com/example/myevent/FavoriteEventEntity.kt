package com.example.myevent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event")
data class FavoriteEventEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    var mediaCover: String? = null,
    var beginTime: String? = null,
)
