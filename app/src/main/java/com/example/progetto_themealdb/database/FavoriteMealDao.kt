package com.example.progetto_themealdb.database

import androidx.room.*
import com.example.progetto_themealdb.model.FavoriteMeal
import kotlinx.coroutines.flow.Flow
//Interfaccia per la gestione della tabella favorite_meal
@Dao
interface FavoriteMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun aggiungiPreferiti(meal: FavoriteMeal)

    @Delete
    suspend fun removeFromFavorites(meal: FavoriteMeal)

    @Query("SELECT * FROM favorite_meals")
    fun getAllFavorites(): Flow<List<FavoriteMeal>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE idMeal = :mealId)")
    fun isFavorite(mealId: String): Flow<Boolean>
}
