package com.example.myevent.api

import com.example.myevent.EventDetailResponse
import com.example.myevent.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getUpcomingEvents(
        @Query("active") active: Int
    ):  Call<EventResponse>

    @GET("events")
    fun getFinishedEvents(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") eventId: Int
    ): Call<EventDetailResponse>

}