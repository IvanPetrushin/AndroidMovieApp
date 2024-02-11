package ru.fintech.services

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.fintech.BuildConfig
import ru.fintech.entities.dto.DescriptionDTO
import ru.fintech.entities.dto.ResponseDTO
import javax.inject.Singleton

@Singleton
interface ApiService {
    @Headers("X-API-KEY: ${BuildConfig.API_KEY}")
    @GET("api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getPopular(
        @Query("page") page: Int = 1
    ): ResponseDTO

    @Headers("X-API-KEY: ${BuildConfig.API_KEY}")
    @GET("api/v2.2/films/{id}")
    suspend fun getDescription(
        @Path("id") id: Int
    ): DescriptionDTO
}