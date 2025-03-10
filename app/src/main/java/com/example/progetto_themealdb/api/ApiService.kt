package com.example.progetto_themealdb.api

import com.example.progetto_themealdb.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): Response<MealResponse>

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): Response<MealResponse>

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): Response<MealResponse>

    @GET("filter.php")
    suspend fun filterByArea(@Query("a") area: String): Response<MealResponse>




}