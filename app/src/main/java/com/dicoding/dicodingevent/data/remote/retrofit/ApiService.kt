package com.dicoding.dicodingevent.data.remote.retrofit

import com.dicoding.dicodingevent.data.remote.response.EventDetailResponse
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events?active=1")
    fun getEventUpcoming(): Call<EventResponse>

    @GET("events?active=0")
    fun getEventFinished():  Call<EventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ):  Call<EventDetailResponse>

    @GET("events?active=-1")
    fun getEventSearch(
        @Query("q") keyword: String
    ): Call<EventResponse>

}