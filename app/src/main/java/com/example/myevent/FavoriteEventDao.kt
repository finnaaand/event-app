package com.example.myevent

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_event")
    fun getAllFavorites(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(event: FavoriteEventEntity)

    @Delete
    suspend fun removeFavorite(event: FavoriteEventEntity)
}
